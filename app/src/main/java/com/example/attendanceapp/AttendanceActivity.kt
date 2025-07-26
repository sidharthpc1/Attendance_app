package com.example.attendanceapp

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import android.util.Log

class AttendanceActivity : BaseActivity() {
    private lateinit var tvFingerprintStatus: TextView

    private val REQUIRED_SSID = "FTTH-3B71"
    private val MIN_RSSI = -65
    private val ESP32_IP = "192.168.1.50"
    private val REQUEST_LOCATION_PERMISSION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        tvFingerprintStatus = findViewById(R.id.tvFingerprintStatus)

        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        val prefs: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        if (!prefs.getBoolean("fingerprint_registered", false)) {
            tvFingerprintStatus.text = "Fingerprint not registered!"
            Toast.makeText(this, "Register fingerprint first.", Toast.LENGTH_LONG).show()
            return
        }

        val ssid = getCurrentSsid()
        val rssi = getCurrentRssi()
        tvFingerprintStatus.text = "Wi‑Fi: $ssid | RSSI: $rssi dBm"

        if (ssid != REQUIRED_SSID) {
            tvFingerprintStatus.text = "Wrong Wi‑Fi (got: $ssid)"
            Toast.makeText(this, "Connect to $REQUIRED_SSID", Toast.LENGTH_LONG).show()
            return
        }

        if (rssi < MIN_RSSI) {
            tvFingerprintStatus.text = "Too far (RSSI: $rssi dBm)"
            Toast.makeText(this, "Move closer to the router to mark attendance", Toast.LENGTH_LONG).show()
            return
        }

        val executor: Executor = ContextCompat.getMainExecutor(this)
        BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Fingerprint accepted", Toast.LENGTH_SHORT).show()
                    sendAttendanceWithPref(prefs)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    tvFingerprintStatus.text = "Auth error: $errString"
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    tvFingerprintStatus.text = "Auth failed"
                }
            }
        ).authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint to Mark Attendance")
                .setNegativeButtonText("Cancel")
                .build()
        )
    }

    private fun sendAttendanceWithPref(prefs: SharedPreferences) {
        Thread {
            try {
                val name = URLEncoder.encode(prefs.getString("name", "Unknown") ?: "Unknown", "UTF-8")
                val regno = URLEncoder.encode(prefs.getString("regno", "0000") ?: "0000", "UTF-8")
                val department = URLEncoder.encode(prefs.getString("department", "N/A") ?: "N/A", "UTF-8")
                val userClass = URLEncoder.encode(prefs.getString("userclass", "N/A") ?: "N/A", "UTF-8")
                val time = URLEncoder.encode(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                    "UTF-8"
                )

                val url = "http://$ESP32_IP/mark?name=$name&regno=$regno&department=$department&class=$userClass&time=$time"
                Log.d("ATTENDANCE_URL", url) // Optional: for debugging

                val conn = (URL(url).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                val code = conn.responseCode
                runOnUiThread {
                    tvFingerprintStatus.text = if (code == 200)
                        "✅ Attendance sent"
                    else
                        "❌ Failed ($code)"
                }
                conn.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    tvFingerprintStatus.text = "❌ Error: ${e.localizedMessage}"
                }
            }
        }.start()
    }


    private fun hasLocationPermission() = (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            )

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) recreate()
    }

    private fun getCurrentSsid(): String {
        val info = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo
        return info.ssid?.replace("\"", "") ?: "<unknown>"
    }

    private fun getCurrentRssi(): Int {
        val info = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo
        return info.rssi
    }
}
