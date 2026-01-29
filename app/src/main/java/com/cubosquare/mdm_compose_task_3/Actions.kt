package com.cubosquare.mdm_compose_task_3



/**
 * Central place to define all Intent action strings and extras.
 * These constants are used for communication between UI,
 * BroadcastReceiver, and background Services.
 */
object Actions {

    /**
     * Broadcast action fired whenever app blocking state changes
     * (for example, when user toggles a switch in the UI).
     */
    const val ACTION_BLOCK_APPS_CHANGED =
        "com.cubosquare.mdm_compose_task_3.BLOCK_APPS_CHANGED"
    /**
     * Extra key used to pass the list or map of blocked apps
     * from UI to receiver/service.
     */
    const val EXTRA_BLOCK_APPS =
        "extra_block_apps"

    /**
     * Action specifically used to enable/disable YouTube blocking.
     */
    const val ACTION_BLOCK_YOUTUBE =
        "com.cubosquare.mdm_compose_task_3.BLOCK_YOUTUBE"




    /**
     * Action specifically used to enable/disable Camera blocking.
     */
    const val ACTION_BLOCK_CAMERA =
        "com.cubosquare.mdm_compose_task_3.BLOCK_CAMERA"
}
