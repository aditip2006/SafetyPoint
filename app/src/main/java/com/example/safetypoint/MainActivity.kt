package com.example.safetypoint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("contacts", MODE_PRIVATE)
        val phone1 = prefs.getString("phone1", "")

        // 🔴 If no contacts → go to contacts screen
        if (phone1.isNullOrEmpty()) {
            startActivity(Intent(this, ContactsActivity::class.java))
            finish()
        }

        // 🔐 Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }

        val activateButton = findViewById<Button>(R.id.activateButton)
        val safeButton = findViewById<Button>(R.id.safeButton)
        val suspiciousButton = findViewById<Button>(R.id.suspiciousButton)
        val helpButton = findViewById<Button>(R.id.helpButton)
        val rideInput = findViewById<EditText>(R.id.rideLinkInput)

        statusText = findViewById(R.id.statusText)

        // 🚗 Activate Ride
        activateButton.setOnClickListener {
            Toast.makeText(this, "Ride Activated", Toast.LENGTH_SHORT).show()
            statusText.text = "Tracking Enabled"
        }

        // ✅ Safe
        safeButton.setOnClickListener {
            statusText.text = "Safe but delayed"
        }

        // 🟡 Suspicious
        suspiciousButton.setOnClickListener {

            Toast.makeText(this, "Sending alert...", Toast.LENGTH_SHORT).show()

            val rideLink = rideInput.text.toString().trim()

            if (rideLink.isEmpty()) {
                Toast.makeText(this, "Paste ride link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            getLocation { location: String ->

                val message = """
⚠️ ALERT: I feel unsafe!
Ride: $rideLink
Location: $location
""".trimIndent()

                // 🔥 FORCE SEND SMS
                sendSMS(message)

                // 🔥 ALSO OPEN SMS APP (BACKUP)
                val prefs = getSharedPreferences("contacts", MODE_PRIVATE)
                val num = prefs.getString("phone1", "")

                if (!num.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = "sms:$num".toUri()
                    intent.putExtra("sms_body", message)
                    startActivity(intent)
                }

                // 📍 Open police map
                val uri = "geo:0,0?q=police+station".toUri()
                startActivity(Intent(Intent.ACTION_VIEW, uri))

                statusText.text = "Alert sent"
            }
        }

        // 🔴 HELP
        helpButton.setOnClickListener {

            Toast.makeText(this, "HELP Clicked", Toast.LENGTH_SHORT).show()

            val rideLink = rideInput.text.toString()

            if (rideLink.isEmpty()) {
                Toast.makeText(this, "Paste ride link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            getLocation { location: String ->

                val message = """
🚨 EMERGENCY!
Ride: $rideLink
Location: $location
""".trimIndent()

                sendSMS(message)

                val uri = "geo:0,0?q=hospital".toUri()
                startActivity(Intent(Intent.ACTION_VIEW, uri))

                startActivity(Intent(this, EmergencyActivity::class.java))
            }
        }
    }

    // 📩 SEND SMS
    private fun sendSMS(message: String) {

        val prefs = getSharedPreferences("contacts", MODE_PRIVATE)

        val numbers = listOf(
            prefs.getString("phone1", ""),
            prefs.getString("phone2", ""),
            prefs.getString("phone3", "")
        )

        val smsManager = SmsManager.getDefault()

        var success = false

        for (num in numbers) {
            if (!num.isNullOrEmpty()) {
                try {
                    smsManager.sendTextMessage(num, null, message, null, null)
                    success = true
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed for $num", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (success) {
            Toast.makeText(this, "Emergency SMS Sent", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No contacts found!", Toast.LENGTH_LONG).show()
        }
    }

    // 📍 GET LOCATION (STRONG VERSION)
    private fun getLocation(callback: (String) -> Unit) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback("Permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {

                val link = "https://maps.google.com/?q=${location.latitude},${location.longitude}"
                callback(link)

            } else {
                // 🔥 fallback
                callback("https://maps.google.com/?q=0,0")
            }
        }
    }
}