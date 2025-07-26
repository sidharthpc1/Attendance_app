package com.example.attendanceapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class FingerprintRegistrationActivity : AppCompatActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_registration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putBoolean("fingerprint_registered", true)
                        .apply()
                    Toast.makeText(this@FingerprintRegistrationActivity, "Fingerprint Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@FingerprintRegistrationActivity, "Fingerprint Registration Failed", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Register Fingerprint")
            .setSubtitle("Touch the fingerprint sensor")
            .setNegativeButtonText("Cancel")
            .build()

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
