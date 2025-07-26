package com.example.attendanceapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var nameEt: EditText
    private lateinit var regEt: EditText
    private lateinit var rollEt: EditText
    private lateinit var deptEt: EditText
    private lateinit var classEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // If user is already registered, redirect to the main activity
        if (prefs.getBoolean("isRegistered", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Set the content view to the register activity layout
        setContentView(R.layout.activity_register)

        // Initialize the EditTexts and Button
        nameEt = findViewById(R.id.etName)
        regEt = findViewById(R.id.etRegNo)
        rollEt = findViewById(R.id.etRollNo)
        deptEt = findViewById(R.id.etDepartment)
        classEt = findViewById(R.id.etClass)
        phoneEt = findViewById(R.id.etPhone)
        btnRegister = findViewById(R.id.btnRegister)

        // Set an onClick listener on the register button
        btnRegister.setOnClickListener {
            // Get the text input from all the fields
            val name = nameEt.text.toString().trim()
            val regno = regEt.text.toString().trim()
            val roll = rollEt.text.toString().trim()
            val dept = deptEt.text.toString().trim()
            val userClass = classEt.text.toString().trim()
            val phone = phoneEt.text.toString().trim()

            // Validate user input
            if (name.isEmpty() || regno.isEmpty() || phone.length < 10) {
                Toast.makeText(this, "Please enter all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save user information in SharedPreferences
            prefs.edit().apply {
                putBoolean("isRegistered", true)
                putString("name", name)
                putString("regno", regno)
                putString("rollno", roll)
                putString("department", dept)
                putString("userclass", userClass)
                putString("phone", phone)
                apply()
            }

            // Show success message
            Toast.makeText(this, "✅ Registered!", Toast.LENGTH_SHORT).show()

            // Redirect to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
