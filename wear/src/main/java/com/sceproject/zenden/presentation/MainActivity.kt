/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.sceproject.zenden.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sceproject.zenden.presentation.app.ZenDenApp
import com.sceproject.zenden.presentation.data.HealthServicesRepository

const val TAG = "ZenDen"
const val PERMISSION = android.Manifest.permission.BODY_SENSORS

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        val healthServicesRepository by lazy { HealthServicesRepository(this) }
        setContent {
            ZenDenApp(
                application = application,
                healthServicesRepository = healthServicesRepository
            )
        }
    }
}


