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


/**
 * Foreground service responsible for enforcing app blocking rules.
 * It continuously monitors foreground app usage and reacts
 * when restricted apps (YouTube or Camera) are launched.
 */
class AppBlockerHandlerService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    // Flags to track which features are currently blocked
    private var blockYouTube = false
    private var blockCamera = false

    // Package names of apps to be blocked
    private val youtubePackage = "com.google.android.youtube"
    private val cameraPackages = listOf(
        "com.android.camera", // AOSP Camera
        "com.android.camera2", // Camera2
        "com.oplus.camera", // Oppo / Realme
        "com.miui.camera" // Xiaomi
    )

    override fun onBind(intent: Intent?): IBinder? = null

    private var isBlocking = false




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


// Read blocking states sent from BroadcastReceiver
        blockYouTube = intent?.getBooleanExtra(Actions.ACTION_BLOCK_YOUTUBE, false) ?: blockYouTube
        blockCamera = intent?.getBooleanExtra(Actions.ACTION_BLOCK_CAMERA, false) ?: blockCamera


// Start service as foreground to avoid being killed by system
        startForeground(
            1001,
            createNotification("YouTube: $blockYouTube | Camera: $blockCamera")
        )


// Start or stop monitoring based on current blocking state
        if (blockYouTube || blockCamera) {
            startMonitoring()
        } else {
            stopMonitoring()
        }
        return START_STICKY
    }

    /**
     * Creates a persistent notification required for foreground services.
     */
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
    /**
     * Runnable that periodically checks which app moved to foreground.
     */
    private val monitorRunnable = object : Runnable {
        override fun run() {
            if (blockYouTube || blockCamera) {
                checkAndBlockApps()
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



    /**
     * Checks foreground apps and redirects user to home
     * if a blocked app is opened.
     */
    private fun checkAndBlockApps() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 2000


        val events = usageStatsManager.queryEvents(beginTime, endTime)
        val event = UsageEvents.Event()


        while (events.hasNextEvent()) {
            events.getNextEvent(event)


            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {


// Block YouTube if enabled
                if (blockYouTube && event.packageName == youtubePackage) {
                    redirectToHome()
                }


// Block Camera apps if enabled
if (blockCamera && cameraPackages.contains(event.packageName)) {
redirectToHome()
}
}
}
}

/**
 * Sends user back to home screen to prevent app usage.
 */
private fun redirectToHome() {
    val homeIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(homeIntent)
}
}
