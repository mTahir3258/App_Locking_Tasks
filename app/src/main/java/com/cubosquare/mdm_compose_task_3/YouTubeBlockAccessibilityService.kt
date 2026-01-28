package com.cubosquare.mdm_compose_task_3


import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class YouTubeBlockAccessibilityService : AccessibilityService() {

    private val youtubePackage = "com.google.android.youtube"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("YTBlockService", "Accessibility event: ${event?.packageName}")
        if (event?.packageName == "com.google.android.youtube") {
            val intent = Intent(this, BlockScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }


    override fun onInterrupt() {}
}
