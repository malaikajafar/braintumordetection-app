package com.example.braintumordetection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        val feedbackButton: Button = findViewById(R.id.feedbackButton)
        val viewStagesButton: Button = findViewById(R.id.viewStagesButton)
        val treatmentOptionsButton: Button = findViewById(R.id.treatmentOptionsButton)

        // ✅ Get data from Intent
        val predictionResult = intent.getStringExtra("predictionResult") ?: "Unknown"
        val imageUrl = intent.getStringExtra("imageUri")



        // ✅ Button listeners
        feedbackButton.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        viewStagesButton.setOnClickListener {
            startActivity(Intent(this, TumorStagesActivity::class.java))
        }

        treatmentOptionsButton.setOnClickListener {
            startActivity(Intent(this, TreatmentOptionsActivity::class.java))
        }
    }
}
