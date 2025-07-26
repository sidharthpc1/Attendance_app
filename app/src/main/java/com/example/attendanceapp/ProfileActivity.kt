package com.example.attendanceapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvRegNo: TextView
    private lateinit var tvRollNo: TextView
    private lateinit var tvDepartment: TextView
    private lateinit var tvClass: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnRegisterFingerprint: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvRegNo = findViewById(R.id.tvRegNo)
        tvRollNo = findViewById(R.id.tvRollNo)
        tvDepartment = findViewById(R.id.tvDepartment)
        tvClass = findViewById(R.id.tvClass)
        tvPhone = findViewById(R.id.tvPhone)
        btnRegisterFingerprint = findViewById(R.id.btnRegisterFingerprint)

        val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        tvName.text = "Name: ${prefs.getString("name", "N/A")}"
        tvRegNo.text = "Reg No: ${prefs.getString("regno", "N/A")}"
        tvRollNo.text = "Roll No: ${prefs.getString("rollno", "N/A")}"
        tvDepartment.text = "Department: ${prefs.getString("department", "N/A")}"
        tvClass.text = "Class: ${prefs.getString("userclass", "N/A")}"
        tvPhone.text = "Phone: ${prefs.getString("phone", "N/A")}"

        btnRegisterFingerprint.setOnClickListener {
            val isRegistered = prefs.getBoolean("fingerprint_registered", false)
            if (isRegistered) {
                Toast.makeText(this, "Fingerprint Already Registered", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, FingerprintRegistrationActivity::class.java))
            }
        }
    }
}
