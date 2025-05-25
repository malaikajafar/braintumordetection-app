package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PatientProfileActivity : AppCompatActivity() {

    private lateinit var btnNotification: Button
    private lateinit var btnViewHistory: Button
    private lateinit var btnViewTips: Button
    private lateinit var btnSecurityActivity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Initialize buttons
        btnNotification = findViewById(R.id.btnNotification)
        btnViewHistory = findViewById(R.id.btnViewHistory)
        btnViewTips = findViewById(R.id.btnViewTips)
        btnSecurityActivity = findViewById(R.id.goToSecurityButton)
        // Set up listeners
        btnNotification.setOnClickListener {
            Toast.makeText(this, "Opening Notifications...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        btnViewHistory.setOnClickListener {
            Toast.makeText(this, "Viewing History...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HistoryTrackingActivity::class.java)
            startActivity(intent)
        }

        btnViewTips.setOnClickListener {
            Toast.makeText(this, "Loading Health Tips...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TipsRecommendationsActivity::class.java)
            startActivity(intent)
        }
        btnSecurityActivity.setOnClickListener {
            Toast.makeText(this, "Loading Health Tips...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PrivacySecurityActivity::class.java)
            startActivity(intent)
        }
    }
}
