/*
package com.example.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        val nameInput = findViewById<EditText>(R.id.editTextName)
        val deptInput = findViewById<EditText>(R.id.editTextDept)
        val rollNoInput = findViewById<EditText>(R.id.editTextRollNo)
        val emailInput = findViewById<EditText>(R.id.editTextEmail)

        val name = intent.getStringExtra("name")
        val dept = intent.getStringExtra("dept")
        val rollNo = intent.getStringExtra("rollNo")
        val email = intent.getStringExtra("email")

        nameInput.setText(name)
        deptInput.setText(dept)
        rollNoInput.setText(rollNo)
        emailInput.setText(email)

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val editedName = nameInput.text.toString()
            val editedDept = deptInput.text.toString()
            val editedRollNo = rollNoInput.text.toString()
            val editedEmail = emailInput.text.toString()

            val intent = Intent(this, ConfirmDetailsActivity::class.java).apply {
                putExtra("name", editedName)
                putExtra("dept", editedDept)
                putExtra("rollNo", editedRollNo)
                putExtra("email", editedEmail)
            }
            startActivity(intent)
            finish()
        }
    }
}
*/