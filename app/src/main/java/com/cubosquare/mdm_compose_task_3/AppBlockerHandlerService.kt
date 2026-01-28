package com.cubosquare.mdm_compose_task_3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.app.usage.UsageStatsManager
import android.app.usage.UsageEvents
import android.os.Handler
import android.os.Looper

class AppBlockerHandlerService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var isBlocking = false
    private val youtubePackage = "com.google.android.youtube"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        isBlocking = intent?.getBooleanExtra(Actions.EXTRA_BLOCK_APPS, false) ?: false

        startForeground(1001, createNotification("YouTube Blocking: $isBlocking"))

        if (isBlocking) {
            startMonitoring()
        } else {
            stopMonitoring()
        }

        return START_STICKY
    }

    private fun createNotification(text: String): Notification {
        val channelId = "block_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "App Blocker",
                NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Blocking Service Active")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    private val monitorRunnable = object : Runnable {
        override fun run() {
            if (isBlocking) {
                checkAndBlockYouTube()
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun startMonitoring() {
        handler.post(monitorRunnable)
    }

    private fun stopMonitoring() {
        handler.removeCallbacks(monitorRunnable)
    }

    private fun checkAndBlockYouTube() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 2000 // last 2 seconds
        val events = usageStatsManager.queryEvents(beginTime, endTime)
        val event = UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND &&
                event.packageName == youtubePackage) {

                // Bring user back to home if YouTube is opened
                val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(homeIntent)
            }
        }
    }
}
