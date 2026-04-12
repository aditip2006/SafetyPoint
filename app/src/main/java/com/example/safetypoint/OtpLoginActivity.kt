package com.example.safetypoint

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class OtpLoginActivity : AppCompatActivity() {

    private var generatedOtp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_login)

        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val otpInput = findViewById<EditText>(R.id.otpInput)
        val sendOtpBtn = findViewById<Button>(R.id.sendOtpBtn)
        val verifyBtn = findViewById<Button>(R.id.verifyBtn)

        sendOtpBtn.setOnClickListener {
            generatedOtp = (1000..9999).random().toString()
            Toast.makeText(this, "OTP: $generatedOtp", Toast.LENGTH_LONG).show()
        }

        verifyBtn.setOnClickListener {
            if (otpInput.text.toString() == generatedOtp) {
                startActivity(Intent(this, ContactsActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
}