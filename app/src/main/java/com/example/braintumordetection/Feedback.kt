package com.example.braintumordetection

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackActivity : AppCompatActivity() {

    private lateinit var btnThumbsUp: Button
    private lateinit var btnThumbsDown: Button
    private lateinit var editFeedbackText: EditText
    private lateinit var btnCancel: Button
    private lateinit var btnSubmit: Button

    private lateinit var db: FirebaseFirestore
    private var isThumbsUp: Boolean? = null // null = no selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize views
        btnThumbsUp = findViewById(R.id.btnThumbsUp)
        btnThumbsDown = findViewById(R.id.btnThumbsDown)
        editFeedbackText = findViewById(R.id.editFeedbackText)
        btnCancel = findViewById(R.id.btnCancel)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Feedback selection logic
        btnThumbsUp.setOnClickListener {
            isThumbsUp = true
            btnThumbsUp.setBackgroundColor(getColor(R.color.teal_200))
            btnThumbsDown.setBackgroundColor(getColor(android.R.color.white))
        }

        btnThumbsDown.setOnClickListener {
            isThumbsUp = false
            btnThumbsDown.setBackgroundColor(getColor(R.color.teal_200))
            btnThumbsUp.setBackgroundColor(getColor(android.R.color.white))
        }

        // Cancel button logic
        btnCancel.setOnClickListener {
            finish()
        }

        // Submit button logic
        btnSubmit.setOnClickListener {
            val feedbackText = editFeedbackText.text.toString().trim()

            if (isThumbsUp == null) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val feedbackData = hashMapOf(
                "thumbsUp" to isThumbsUp,
                "feedbackText" to feedbackText,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Feedback submitted. Thank you!", Toast.LENGTH_SHORT).show()

                    // Reset UI
                    isThumbsUp = null
                    editFeedbackText.text.clear()
                    btnThumbsUp.setBackgroundColor(getColor(android.R.color.white))
                    btnThumbsDown.setBackgroundColor(getColor(android.R.color.white))

                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
