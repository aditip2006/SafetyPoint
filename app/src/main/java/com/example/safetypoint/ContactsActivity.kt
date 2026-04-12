package com.example.safetypoint

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val phone1 = findViewById<EditText>(R.id.phone1)
        val phone2 = findViewById<EditText>(R.id.phone2)
        val phone3 = findViewById<EditText>(R.id.phone3)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        val prefs = getSharedPreferences("contacts", MODE_PRIVATE)

        saveBtn.setOnClickListener {

            // 🔥 TRIM SPACES (IMPORTANT FIX)
            val p1 = phone1.text.toString().trim()
            val p2 = phone2.text.toString().trim()
            val p3 = phone3.text.toString().trim()

            // 🚨 REQUIRE AT LEAST ONE CONTACT
            if (p1.isEmpty()) {
                Toast.makeText(this, "Enter at least one contact", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ 10 digit validation
            if (p1.length != 10) {
                Toast.makeText(this, "Phone 1 must be 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (p2.isNotEmpty() && p2.length != 10) {
                Toast.makeText(this, "Phone 2 must be 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (p3.isNotEmpty() && p3.length != 10) {
                Toast.makeText(this, "Phone 3 must be 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 💾 SAVE CONTACTS
            prefs.edit()
                .putString("phone1", p1)
                .putString("phone2", p2)
                .putString("phone3", p3)
                .apply()

            // 🔍 DEBUG (VERY IMPORTANT)
            Toast.makeText(this, "Saved: $p1", Toast.LENGTH_LONG).show()

            // 🚀 GO TO MAIN
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}