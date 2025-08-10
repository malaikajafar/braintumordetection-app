package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TreatmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.treatment)

        // Get tumor type from intent
        val tumorType = intent.getStringExtra("tumorType") ?: "Unknown"

        // Set treatment text dynamically based on tumor type
        val treatmentTextView = findViewById<TextView>(R.id.treatmentTextView)
        val treatment = when (tumorType.lowercase()) {
            "glioma" -> "Treatment for Glioma:\n• Surgery\n• Radiation Therapy\n• Chemotherapy"
            "meningioma" -> "Treatment for Meningioma:\n• Observation\n• Surgery\n• Radiation"
            "pituitary" -> "Treatment for Pituitary Tumor:\n• Hormone Therapy\n• Surgery\n• Radiation"
            "no tumor" -> "No tumor detected. No treatment needed."
            else -> "Consult a neurologist for further advice."
        }
        treatmentTextView.text = treatment

        // Button to go to View History screen
        val viewHistoryButton = findViewById<Button>(R.id.viewHistoryButton)
        viewHistoryButton.setOnClickListener {
            val intent = Intent(this, HistoryTrackingActivity::class.java)
            startActivity(intent)
        }

        // Button to go to AI Tips screen
        val aiTipsButton = findViewById<Button>(R.id.aiTipsButton)
        aiTipsButton.setOnClickListener {
            val intent = Intent(this, TipsRecommendationsActivity::class.java)
            startActivity(intent)
        }
    }
}
