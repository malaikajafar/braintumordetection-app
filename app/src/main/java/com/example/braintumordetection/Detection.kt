package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.PatientProfileActivity

class DetectionActivity : AppCompatActivity() {

    private lateinit var btnViewProfile: Button
    private lateinit var btnStartDetection: Button
    private lateinit var btnDashboard: Button
    private lateinit var btnUpload: Button // New button for UploadActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detection)

        // Initialize buttons
        btnViewProfile = findViewById(R.id.btnViewProfile)
        btnStartDetection = findViewById(R.id.btnStartDetection)
        btnDashboard = findViewById(R.id.btnDashboard)


        // View Profile button click listener
        btnViewProfile.setOnClickListener {
            // Navigate to Profile Activity
            val intent = Intent(this,PatientProfileActivity::class.java)
            startActivity(intent)
        }

        // Start Detection button click listener
        btnStartDetection.setOnClickListener {
            // Navigate to Start Detection Activity (or relevant activity)
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        // Dashboard button click listener
        btnDashboard.setOnClickListener {
            // Navigate to Dashboard Activity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }



    }
}
