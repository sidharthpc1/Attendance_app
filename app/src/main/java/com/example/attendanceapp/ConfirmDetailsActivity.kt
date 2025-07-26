package com.example.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ConfirmDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_details)

        val nameTextView: TextView = findViewById(R.id.textViewName)
        val deptTextView: TextView = findViewById(R.id.textViewDept)
        val rollNoTextView: TextView = findViewById(R.id.textViewRollNo)
        val emailTextView: TextView = findViewById(R.id.textViewEmail)
        val btnConfirm: Button = findViewById(R.id.btnConfirm)

        // Get data from intent
        val name = intent.getStringExtra("name")
        val dept = intent.getStringExtra("dept")
        val rollno = intent.getStringExtra("rollno")
        val email = intent.getStringExtra("email")

        // Set data to TextViews
        nameTextView.text = "Name: $name"
        deptTextView.text = "Department: $dept"
        rollNoTextView.text = "Roll No: $rollno"
        emailTextView.text = "Email: $email"

        // Confirm button click
        btnConfirm.setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }
    }
}
