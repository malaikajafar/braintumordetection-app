package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Initialize buttons
        val startDetectionButton = findViewById<Button>(R.id.startDetectionButton)
        val viewResultsButton = findViewById<Button>(R.id.viewResultsButton)
        val goToProfileButton = findViewById<Button>(R.id.goToProfileButton)
        val goToNotificationsButton = findViewById<Button>(R.id.goToNotificationsButton)
        val goToFeedbackButton = findViewById<Button>(R.id.goToFeedbackButton)
        val goToSecurityButton = findViewById<Button>(R.id.goToSecurityButton)

        // Set click listeners
        startDetectionButton.setOnClickListener {
            startActivity(Intent(this, DetectionActivity::class.java))
        }

        viewResultsButton.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        goToProfileButton.setOnClickListener {
            startActivity(Intent(this, PatientProfileActivity::class.java))
        }

        goToNotificationsButton.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        goToFeedbackButton.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        goToSecurityButton.setOnClickListener {
            startActivity(Intent(this, PrivacySecurityActivity::class.java))
        }
    }
}
