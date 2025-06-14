package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // View bindings
        fullNameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signupButton)

        signUpButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInputs(fullName, email, password)) {
                signUpUser(fullName, email, password)
            }
        }
    }

    private fun validateInputs(fullName: String, email: String, password: String): Boolean {
        return when {
            fullName.isEmpty() -> {
                showToast("Please enter your full name")
                false
            }
            email.isEmpty() -> {
                showToast("Please enter your email")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email format")
                false
            }
            password.isEmpty() -> {
                showToast("Please enter your password")
                false
            }
            password.length < 6 -> {
                showToast("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    private fun signUpUser(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Update user profile with display name
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName)
                            .build()

                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    // Save additional user data to Firestore
                                    saveUserToFirestore(user.uid, fullName, email)
                                } else {
                                    handleError("Failed to update profile", profileTask.exception)
                                }
                            }
                    } else {
                        handleError("User creation failed - null user", null)
                    }
                } else {
                    handleError("Authentication failed", authTask.exception)
                }
            }
    }

    private fun saveUserToFirestore(userId: String, fullName: String, email: String) {
        val userData = hashMapOf(
            "fullName" to fullName,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users").document(userId)
            .set(userData)
            .addOnSuccessListener {
                // Send email verification
                auth.currentUser?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Log.d("SignUp", "Verification email sent")
                        }
                        // Navigate to DetectionActivity regardless of verification status
                        navigateToDetection()
                    }
            }
            .addOnFailureListener { e ->
                handleError("Failed to save user data", e)
                // Optional: Delete the user if Firestore fails
                auth.currentUser?.delete()
            }
    }

    private fun navigateToDetection() {
        startActivity(Intent(this, DetectionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun handleError(message: String, exception: Exception?) {
        Log.e("SignUp", message, exception)
        showToast("Error: ${exception?.message ?: message}")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}