package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TipsRecommendationsActivity : AppCompatActivity() {

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tips)

        val viewDiagnosisButton: Button = findViewById(R.id.viewDiagnosisButton)
        val viewClassificationButton: Button = findViewById(R.id.viewClassificationButton)

        // ✅ Log access to TipsRecommendationsActivity
        logUserActivity("Opened AI Tips & Recommendations")

        viewDiagnosisButton.setOnClickListener {
            Toast.makeText(this, "Opening Diagnosis Details...", Toast.LENGTH_SHORT).show()
            logUserActivity("Clicked Diagnosis Details Button")
            val intent = Intent(this, DiagnosisDetailsActivity::class.java)
            startActivity(intent)
        }

        viewClassificationButton.setOnClickListener {
            Toast.makeText(this, "Opening Classification...", Toast.LENGTH_SHORT).show()
            logUserActivity("Clicked Tumor Classification Button")
            val intent = Intent(this, TumorClassificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logUserActivity(action: String) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val log = hashMapOf(
            "userId" to userId,
            "action" to action,
            "screen" to "TipsRecommendationsActivity",
            "timestamp" to System.currentTimeMillis()
        )
        firestore.collection("userActivityLogs")
            .add(log)
            .addOnSuccessListener {
                // Optionally log success
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Logging failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
