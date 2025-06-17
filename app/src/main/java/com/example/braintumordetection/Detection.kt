package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DetectionActivity : AppCompatActivity() {

    private lateinit var btnViewProfile: Button
    private lateinit var btnStartDetection: Button
    private lateinit var btnDashboard: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is logged in
        if (auth.currentUser == null) {
            redirectToLogin()
            return
        }

        setContentView(R.layout.detection)

        // Initialize buttons
        btnViewProfile = findViewById(R.id.btnViewProfile)
        btnStartDetection = findViewById(R.id.btnStartDetection)
        btnDashboard = findViewById(R.id.btnDashboard)

        // Set click listeners
        btnViewProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity ::class.java))
        }

        btnStartDetection.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        btnDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    private fun redirectToLogin() {
        Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}