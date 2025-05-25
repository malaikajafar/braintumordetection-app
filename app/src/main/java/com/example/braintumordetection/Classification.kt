package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.R
import com.example.braintumordetection.TumorStagesActivity

class TumorClassificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.classification) // XML filename

        // Find buttons by ID
        val viewProfileButton = findViewById<Button>(R.id.viewProfileButton)
        val viewHistoryButton = findViewById<Button>(R.id.viewHistoryButton)

        // Handle "View Profile" button click
        viewProfileButton.setOnClickListener {
            Toast.makeText(this, "View Profile clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, PatientProfileActivity::class.java))
            val intent = Intent(this, PatientProfileActivity::class.java)
            startActivity(intent)
        }

        // Handle "View History" button click
        viewHistoryButton.setOnClickListener {
            Toast.makeText(this, "View History clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, HistoryActivity::class.java))
            val intent = Intent(this, HistoryTrackingActivity::class.java)
            startActivity(intent)
        }
    }
}
