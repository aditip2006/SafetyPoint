package com.example.safetypoint

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }


        val activateButton = findViewById<Button>(R.id.activateButton)
        val safeButton = findViewById<Button>(R.id.safeButton)
        val suspiciousButton = findViewById<Button>(R.id.suspiciousButton)
        val helpButton = findViewById<Button>(R.id.helpButton)
        statusText = findViewById(R.id.statusText)

        activateButton.setOnClickListener {
            statusText.text = "Status: Ride Activated\nCar: MH12AB1234\nDriver: Raj"
            showSafetyNotification()
        }

        safeButton.setOnClickListener {
            statusText.text = "Status: Safe but Delayed"
        }

        suspiciousButton.setOnClickListener {
            statusText.text = "Alert Sent to Emergency Contact"
        }

        helpButton.setOnClickListener {
            statusText.text = "EMERGENCY ALERT SENT!"
        }
    }

    private fun showSafetyNotification() {

        val channelId = "safety_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Safety Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("SafetyPoint Active")
            .setContentText("Emergency controls active")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat.from(this).notify(1, notification)
        }

    }
}
