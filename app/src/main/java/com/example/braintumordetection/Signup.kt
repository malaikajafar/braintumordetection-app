package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var isProcessing = false  // Prevent multiple clicks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        fullNameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signupButton)

        signUpButton.setOnClickListener {
            if (isProcessing) return@setOnClickListener

            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInputs(fullName, email, password)) {
                isProcessing = true
                signUpButton.isEnabled = false
                createFirebaseAccount(fullName, email, password)
            }
        }
    }

    private fun validateInputs(fullName: String, email: String, password: String): Boolean {
        return when {
            fullName.isEmpty() -> {
                toast("Enter full name")
                false
            }
            email.isEmpty() -> {
                toast("Enter email")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                toast("Invalid email")
                false
            }
            password.isEmpty() -> {
                toast("Enter password")
                false
            }
            password.length < 6 -> {
                toast("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    private fun createFirebaseAccount(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                if (user != null) {
                    updateUserProfile(user, fullName)
                } else {
                    toast("Signup failed. Try again.")
                    resetButton()
                }
            }
            .addOnFailureListener { e ->
                val message = e.message ?: "Unknown error"
                if (message.contains("email address is already in use", ignoreCase = true)) {
                    toast("This email is already registered.")
                } else {
                    toast("Signup error: $message")
                }
                resetButton()
            }
    }

    private fun updateUserProfile(user: FirebaseUser, fullName: String) {
        val updates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()

        user.updateProfile(updates)
            .addOnSuccessListener {
                saveUserToFirestore(user.uid, fullName, user.email ?: "")
            }
            .addOnFailureListener { e ->
                toast("Profile update failed: ${e.message}")
                user.delete()
                resetButton()
            }
    }

    private fun saveUserToFirestore(userId: String, fullName: String, email: String) {
        val trialStartDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
// 🎁 Start trial today

        val userMap = hashMapOf(
            "fullName" to fullName,
            "email" to email,
            "createdAt" to System.currentTimeMillis(),
            "trial_start_date" to trialStartDate
        )

        firestore.collection("profile").document(userId).set(userMap)
            .addOnSuccessListener {
                sendEmailVerification(auth.currentUser!!)
            }
            .addOnFailureListener { e ->
                toast("Failed to save user data: ${e.message}")
                resetButton()
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener {
                toast("Verification email sent to ${user.email}")
                navigateToNextScreen()
            }
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this, DetectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun resetButton() {
        isProcessing = false
        signUpButton.isEnabled = true
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
