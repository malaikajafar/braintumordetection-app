package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.R
import com.example.braintumordetection.HistoryTrackingActivity
import com.example.braintumordetection.TipsRecommendationsActivity

class TreatmentOptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.treatment) // Make sure this matches your XML file name

        val viewHistoryButton: Button = findViewById(R.id.viewHistoryButton)
        val aiTipsButton: Button = findViewById(R.id.aiTipsButton)

        // View History Button Click
        viewHistoryButton.setOnClickListener {
            Toast.makeText(this, "Opening Patient History...", Toast.LENGTH_SHORT).show()
            // TODO: Replace with actual intent if HistoryActivity exists
            // startActivity(Intent(this, HistoryActivity::class.java))
            val intent = Intent(this, HistoryTrackingActivity::class.java)
            startActivity(intent)
        }

        // AI Tips Button Click
        aiTipsButton.setOnClickListener {
            Toast.makeText(this, "Opening AI Tips...", Toast.LENGTH_SHORT).show()
            // TODO: Replace with actual intent if AITipsActivity exists
            // startActivity(Intent(this, AITipsActivity::class.java))
            val intent = Intent(this, TipsRecommendationsActivity::class.java)
            startActivity(intent)
        }
    }
}
