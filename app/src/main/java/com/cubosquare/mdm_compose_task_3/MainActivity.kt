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

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            var youtubeBlocked by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                        onCheckedChange = {
                            youtubeBlocked = it
                            val intent = Intent(Actions.ACTION_BLOCK_APPS_CHANGED).apply {
                                putExtra(Actions.EXTRA_BLOCK_APPS, it)
                            }
                            sendBroadcast(intent)
                        }
                    )
                }
            }        }
    }
}
