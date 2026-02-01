package com.example.epsonprint.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {

    private var callback: ((Boolean) -> Unit)? = null

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }
        callback?.invoke(granted)
    }

    fun requestWifiRelated(onResult: (Boolean) -> Unit) {
        callback = onResult
        val perms = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE
        )
        if (Build.VERSION.SDK_INT >= 33) {
            perms.add("android.permission.NEARBY_WIFI_DEVICES")
        }
        val need = perms.any {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (need) {
            launcher.launch(perms.toTypedArray())
        } else {
            onResult(true)
        }
    }
}
