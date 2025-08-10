package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var adviceTextView: TextView
    private lateinit var feedbackButton: Button
    private lateinit var viewStagesButton: Button
    private lateinit var treatmentOptionsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        resultTextView = findViewById(R.id.resultTextView)
        adviceTextView = findViewById(R.id.adviceTextView)
        feedbackButton = findViewById(R.id.feedbackButton)
        viewStagesButton = findViewById(R.id.viewStagesButton)
        treatmentOptionsButton = findViewById(R.id.treatmentOptionsButton)

        val predictedClass = intent.getStringExtra("predictionResult") ?: "Unknown"
        val confidence = intent.getDoubleExtra("confidence", 0.0)
        val advice = intent.getStringExtra("advice") ?: ""

        val displayText = "Prediction: $predictedClass\nConfidence: ${"%.2f".format(confidence * 100)}%"
        resultTextView.text = displayText
        adviceTextView.text = advice

        feedbackButton.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        viewStagesButton.setOnClickListener {
            startActivity(Intent(this, TumorStagesActivity::class.java))
        }

        treatmentOptionsButton.setOnClickListener {
            startActivity(Intent(this, TreatmentActivity::class.java))
        }
    }
}
