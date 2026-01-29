package com.cubosquare.mdm_compose_task_3


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/**
 * BroadcastReceiver that listens for blocking actions
 * (YouTube or Camera) sent from the UI layer.
 *
 * Based on the received action, it forwards the request
 * to the foreground service that actually enforces
 * the blocking logic.
 */
class AppBlockReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // Read whether blocking is enabled or disabled from the Intent
        val enabled = intent.getBooleanExtra(Actions.EXTRA_BLOCK_APPS, false)
        // Decide which type of blocking is requested based on action
        val serviceIntent = Intent(context, AppBlockerHandlerService::class.java).apply {

            when(intent.action){
                // YouTube block / unblock request
           Actions.ACTION_BLOCK_YOUTUBE -> {
               putExtra(Actions.ACTION_BLOCK_YOUTUBE, enabled)
           }

                Actions.ACTION_BLOCK_CAMERA ->{
                    putExtra(Actions.ACTION_BLOCK_CAMERA, enabled)
                }

            }
        }
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}
