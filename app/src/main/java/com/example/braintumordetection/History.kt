package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HistoryTrackingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history) // Ensure this matches your XML file name (history.xml)

        // Corrected Button IDs (must match XML exactly)
        val viewTipsButton = findViewById<Button>(R.id.viewAiTipsButton)
        val viewProfileButton = findViewById<Button>(R.id.viewProfileButton)

        // "View Tips" button action
        viewTipsButton.setOnClickListener {
            Toast.makeText(this, "View Tips clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TipsRecommendationsActivity::class.java)
            startActivity(intent)
        }

        // "View Profile" button action
        viewProfileButton.setOnClickListener {
            Toast.makeText(this, "View Profile clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ProfileSettingsActivity  ::class.java)
            startActivity(intent)
        }
    }
}
