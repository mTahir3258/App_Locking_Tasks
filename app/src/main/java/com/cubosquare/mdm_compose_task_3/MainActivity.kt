// MainActivity.kt
package com.cubosquare.mdm_compose_task_3

// MainActivity.kt

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



/**
 * Main UI screen containing switches to control
 * YouTube and Camera blocking independently.
 *
 * Each switch sends a broadcast that is received
 * by AppBlockReceiver, which then starts the
 * foreground blocking service.
 */
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            // State for YouTube blocking switch
            var youtubeBlocked by remember { mutableStateOf(false) }


           // State for Camera blocking switch
            var cameraBlocked by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                 * YouTube blocking switch
                 */

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = "Block YouTube")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = youtubeBlocked,
                        onCheckedChange = {enabled->
                            youtubeBlocked = enabled

                            //Broadcast Youtube blocking state
                            val intent = Intent(Actions.ACTION_BLOCK_YOUTUBE).apply {
                                putExtra(Actions.EXTRA_BLOCK_APPS, enabled)
                            }
                            sendBroadcast(intent)
                        }
                    )
                }
                /**
                 * Camera blocking switch
                 */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Block Camera"
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Switch(
                        checked = cameraBlocked,
                        onCheckedChange = {enabled ->
                            cameraBlocked = enabled

                            //Broadcast camera blocking state
                            val intent = Intent(Actions.ACTION_BLOCK_CAMERA).apply {
                                putExtra(Actions.EXTRA_BLOCK_APPS,enabled)
                            }
                            sendBroadcast(intent)
                        }
                    )
                }
            }
        }
    }
}
