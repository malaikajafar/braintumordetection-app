package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TreatmentOptionsActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.treatment)

        val viewHistoryButton: Button = findViewById(R.id.viewHistoryButton)
        val aiTipsButton: Button = findViewById(R.id.aiTipsButton)

        // View History Button Click
        viewHistoryButton.setOnClickListener {
            Toast.makeText(this, "Opening Patient History...", Toast.LENGTH_SHORT).show()
            logUserAction("Viewed Patient History")
            startActivity(Intent(this, HistoryTrackingActivity::class.java))
        }

        // AI Tips Button Click
        aiTipsButton.setOnClickListener {
            Toast.makeText(this, "Opening AI Tips...", Toast.LENGTH_SHORT).show()
            logUserAction("Viewed AI Tips")
            startActivity(Intent(this, TipsRecommendationsActivity::class.java))
        }
    }

    // Function to log activity to Firestore
    private fun logUserAction(action: String) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val data = hashMapOf(
            "userId" to userId,
            "action" to action,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("user_activities")
            .add(data)
            .addOnSuccessListener {
                // Optional: log success
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error logging action: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
