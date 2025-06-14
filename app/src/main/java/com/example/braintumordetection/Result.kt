package com.example.braintumordetection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResultActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        val feedbackButton: Button = findViewById(R.id.feedbackButton)
        val viewStagesButton: Button = findViewById(R.id.viewStagesButton)
        val treatmentOptionsButton: Button = findViewById(R.id.treatmentOptionsButton)

        // ✅ Get data from Intent
        val predictionResult = intent.getStringExtra("predictionResult") ?: "Unknown"
        val imageUrl = intent.getStringExtra("imageUri") // May be null if something went wrong



        // ✅ Save prediction to Firestore (for latest uploaded image)
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("detections")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val documentId = document.id
                    firestore.collection("detections")
                        .document(documentId)
                        .update("result", predictionResult)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Result saved: $predictionResult", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error updating result: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error finding document: ${e.message}", Toast.LENGTH_SHORT).show()
            }

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
