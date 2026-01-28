package com.cubosquare.mdm_compose_task_3


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class AppBlockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val enabled = intent.getBooleanExtra(Actions.EXTRA_BLOCK_APPS, false)
        val serviceIntent = Intent(context, AppBlockerHandlerService::class.java).apply {
            putExtra(Actions.EXTRA_BLOCK_APPS, enabled)
        }
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}
