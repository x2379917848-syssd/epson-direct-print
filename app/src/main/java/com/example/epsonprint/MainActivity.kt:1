package com.example.epsonprint

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.epsonprint.databinding.ActivityMainBinding
import com.example.epsonprint.permissions.PermissionHelper
import com.example.epsonprint.print.PrintDispatcher
import com.example.epsonprint.wifi.WifiConnector

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var wifiConnector: WifiConnector

    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            PrintDispatcher.print(this, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionHelper = PermissionHelper(this)
        wifiConnector = WifiConnector(this)

        binding.btnConnect.setOnClickListener {
            requestWifiPermissions {
                val ssid = binding.etSsid.text?.toString()?.trim().orEmpty()
                val pwd = binding.etPassword.text?.toString()?.trim().orEmpty()
                if (ssid.isEmpty()) {
                    Toast.makeText(this, "请输入打印机 SSID", Toast.LENGTH_SHORT).show()
                    return@requestWifiPermissions
                }
                val added = wifiConnector.addSuggestion(ssid, if (pwd.isEmpty()) null else pwd)
                if (!added) {
                    Toast.makeText(this, "提交 Wi‑Fi 建议失败，请检查权限", Toast.LENGTH_SHORT).show()
                }
                openSystemInternetPanel()
            }
        }

        binding.btnCheck.setOnClickListener {
            requestWifiPermissions {
                val ssid = wifiConnector.currentSsid()
                binding.tvStatus.text = "当前 Wi‑Fi：" + (ssid ?: "未知/未连接")
            }
        }

        binding.btnPickAndPrint.setOnClickListener {
            val mimeTypes = arrayOf("application/pdf", "image/*")
            pickFileLauncher.launch(mimeTypes)
        }
    }

    private fun requestWifiPermissions(onGranted: () -> Unit) {
        permissionHelper.requestWifiRelated {
            if (it) onGranted() else {
                Toast.makeText(this, "需要授予位置/Wi‑Fi 权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openSystemInternetPanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
                return
            } catch (_: Exception) { }
        }
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}
