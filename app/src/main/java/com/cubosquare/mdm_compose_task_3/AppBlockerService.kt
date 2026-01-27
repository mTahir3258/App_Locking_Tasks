package com.cubosquare.mdm_compose_task_3
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.view.accessibility.AccessibilityEvent

class AppBlockerService : AccessibilityService() {

    companion object {
        var isBlockingYouTube = false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!isBlockingYouTube) return

        // Check if the user is trying to open YouTube
        val packageName = event.packageName?.toString()
        if (packageName == "com.google.android.youtube") {
            // Kick the user out to the Home Screen
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    override fun onInterrupt() {}
}