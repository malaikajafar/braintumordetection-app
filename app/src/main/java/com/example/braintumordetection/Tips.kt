package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.R
import com.example.braintumordetection.TumorStagesActivity
import com.example.braintumordetection.DiagnosisDetailsActivity
import com.example.braintumordetection.TumorClassificationActivity

class TipsRecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tips) // Change if your layout file has a different name

        val viewDiagnosisButton: Button = findViewById(R.id.viewDiagnosisButton)
        val viewClassificationButton: Button = findViewById(R.id.viewClassificationButton)

        // View Diagnosis button click
        viewDiagnosisButton.setOnClickListener {
            Toast.makeText(this, "Opening Diagnosis Details...", Toast.LENGTH_SHORT).show()
            // TODO: Replace with actual intent if DiagnosisActivity exists
            // startActivity(Intent(this, DiagnosisActivity::class.java))
            val intent = Intent(this, DiagnosisDetailsActivity::class.java)
            startActivity(intent)
        }

        // View Classification button click
        viewClassificationButton.setOnClickListener {
            Toast.makeText(this, "Opening Classification...", Toast.LENGTH_SHORT).show()
            // TODO: Replace with actual intent if ClassificationActivity exists
            // startActivity(Intent(this, ClassificationActivity::class.java))
            val intent = Intent(this, TumorClassificationActivity::class.java)
            startActivity(intent)
        }
    }
}
