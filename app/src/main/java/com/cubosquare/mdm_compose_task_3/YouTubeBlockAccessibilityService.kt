package com.cubosquare.mdm_compose_task_3


import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent



/**
 * Accessibility service that monitors foreground app changes.
 * If a restricted app (YouTube or Camera) is opened,
 * it immediately launches a blocking screen.
 *
 * NOTE: This works as a strong enforcement layer when
 * UsageStats-based blocking is not enough.
 */
class YouTubeBlockAccessibilityService : AccessibilityService() {

    // Package name for YouTube
    private val youtubePackage = "com.google.android.youtube"


    // Common camera package names across devices
    private val cameraPackages = listOf(
        "com.android.camera", // AOSP Camera
        "com.android.camera2", // Camera2
        "com.oplus.camera", // Oppo / Realme
        "com.miui.camera" // Xiaomi
    )
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        // Log events for debugging and verification
        Log.d("BlockAccessibility", "Event from: ${event?.packageName}")

        val packageName = event?.packageName?.toString() ?: return


// If YouTube or Camera is opened, redirect to block screen
        if (packageName == youtubePackage || cameraPackages.contains(packageName)) {
            val intent = Intent(this, BlockScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }



    override fun onInterrupt() {}
}
