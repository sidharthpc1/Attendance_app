package com.example.attendanceapp

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    private val REQUIRED_SSID = "FTTH-3B71" // 🔁 Replace with your actual WiFi name
    private var wifiLayout: LinearLayout? = null

    override fun setContentView(layoutResID: Int) {
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val inflater = LayoutInflater.from(this)

        // ✅ Inflate and add WiFi status layout
        wifiLayout = inflater.inflate(R.layout.include_wifi_status, rootLayout, false) as LinearLayout
        rootLayout.addView(wifiLayout)

        // ✅ Inflate and add actual activity layout
        val contentLayout = inflater.inflate(layoutResID, rootLayout, false)
        rootLayout.addView(contentLayout)

        super.setContentView(rootLayout)

        updateWifiIcon()
    }

    override fun onResume() {
        super.onResume()
        updateWifiIcon() // ✅ Refresh icon when coming back to activity
    }

    private fun updateWifiIcon() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ssid = wifiManager.connectionInfo.ssid.replace("\"", "")
        val ivWifi = wifiLayout?.findViewById<ImageView>(R.id.ivWifiStatus)

        if (ssid == REQUIRED_SSID) {
            ivWifi?.setImageResource(R.drawable.ic_wifi_green)
        } else {
            ivWifi?.setImageResource(R.drawable.ic_wifi_red)
        }
    }
}