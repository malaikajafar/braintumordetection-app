package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TumorStagesActivity : AppCompatActivity() {

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stages)  // Make sure this matches your XML

        logStageActivity() // Firebase logging call

        val viewTreatmentOptionsButton = findViewById<Button>(R.id.viewTreatmentOptionsButton)
        val fullDiagnosisButton = findViewById<Button>(R.id.fullDiagnosisButton)

        viewTreatmentOptionsButton.setOnClickListener {
            val intent = Intent(this, TreatmentOptionsActivity::class.java)
            startActivity(intent)
        }

        fullDiagnosisButton.setOnClickListener {
            val intent = Intent(this, DiagnosisDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logStageActivity() {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val data = hashMapOf(
            "userId" to userId,
            "action" to "Viewed Tumor Stages",
            "screen" to "TumorStagesActivity",
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("userActivityLogs")
            .add(data)
            .addOnSuccessListener {
                // Optional: Logging successful
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to log activity: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
