package com.example.attendanceapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isRegistered = sharedPref.getBoolean("isRegistered", false)
        if (isRegistered) {
            // Already registered → skip welcome
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_welcome)

        // Find buttons
        val btnStartRegister = findViewById<Button>(R.id.btnStartRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnMarkAttendance = findViewById<Button>(R.id.btnMarkAttendance)

        btnStartRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val name = sharedPref.getString("name", null)
            if (!name.isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "No existing registration. Please register first.", Toast.LENGTH_SHORT).show()
            }
        }

        btnMarkAttendance.setOnClickListener {
            val name = sharedPref.getString("name", null)
            if (!name.isNullOrEmpty()) {
                startActivity(Intent(this, AttendanceActivity::class.java))
            } else {
                Toast.makeText(this, "Please register first to mark attendance.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}