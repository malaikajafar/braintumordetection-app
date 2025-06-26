package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TreatmentOptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.treatment)

        val viewHistoryButton: Button = findViewById(R.id.viewHistoryButton)
        val aiTipsButton: Button = findViewById(R.id.aiTipsButton)

        // View History Button Click
        viewHistoryButton.setOnClickListener {
            Toast.makeText(this, "Opening Patient History...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HistoryTrackingActivity::class.java))
        }

        // AI Tips Button Click
        aiTipsButton.setOnClickListener {
            Toast.makeText(this, "Opening AI Tips...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, TipsRecommendationsActivity::class.java))
        }
    }
}
