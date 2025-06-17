package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.R
import com.example.braintumordetection.TumorStagesActivity
import com.example.braintumordetection.ProfileActivity
import com.example.braintumordetection.TumorClassificationActivity

class DiagnosisDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diagnosis) // XML filename

        // Find buttons by ID
        val viewClassificationButton = findViewById<Button>(R.id.viewClassificationButton)
        val viewProfileButton = findViewById<Button>(R.id.viewProfileButton)

        // Handle "View Classification" button click
        viewClassificationButton.setOnClickListener {
            Toast.makeText(this, "View Classification clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, TumorClassificationActivity::class.java))
            val intent = Intent(this, TumorClassificationActivity::class.java)
            startActivity(intent)
        }

        // Handle "View Profile" button click
        viewProfileButton.setOnClickListener {
            Toast.makeText(this, "View Profile clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, PatientProfileActivity::class.java))
            val intent = Intent(this, ProfileActivity ::class.java)
            startActivity(intent)
        }
    }
}
