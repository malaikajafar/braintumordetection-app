package com.example.braintumordetection  // ← Apne actual package name se replace karein

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.R

class TumorStagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stages)  // XML file ka naam sahi hona chahiye

        // Buttons ko find karo
        val viewTreatmentOptionsButton = findViewById<Button>(R.id.viewTreatmentOptionsButton)
        val fullDiagnosisButton = findViewById<Button>(R.id.fullDiagnosisButton)

        // Treatment Options button click handling
        viewTreatmentOptionsButton.setOnClickListener {
            val intent = Intent(this, TreatmentOptionsActivity::class.java)
            startActivity(intent)
        }

        // Full Diagnosis button click handling
        fullDiagnosisButton.setOnClickListener {
            val intent = Intent(this, DiagnosisDetailsActivity::class.java)
            startActivity(intent)
        }
    }
}
