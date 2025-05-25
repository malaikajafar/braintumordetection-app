package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)  // Make sure this matches your layout file

        val feedbackButton: Button = findViewById(R.id.feedbackButton)
        val viewStagesButton: Button = findViewById(R.id.viewStagesButton)
        val treatmentOptionsButton: Button = findViewById(R.id.treatmentOptionsButton)

        // Feedback Button Intent
        feedbackButton.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // View Stages Button Intent
        viewStagesButton.setOnClickListener {
            val intent = Intent(this, TumorStagesActivity::class.java)
            startActivity(intent)
        }

        // Treatment Options Button Intent
        treatmentOptionsButton.setOnClickListener {
            val intent = Intent(this, TreatmentOptionsActivity::class.java)
            startActivity(intent)
        }
    }
}
