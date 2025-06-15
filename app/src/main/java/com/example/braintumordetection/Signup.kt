package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        // Firebase init
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Bind views
        fullNameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signupButton)

        signUpButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInputs(fullName, email, password)) {
                createAccount(fullName, email, password)
            }
        }
    }

    private fun validateInputs(fullName: String, email: String, password: String): Boolean {
        return when {
            fullName.isEmpty() -> {
                toast("Please enter your full name")
                false
            }
            email.isEmpty() -> {
                toast("Please enter your email")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                toast("Invalid email format")
                false
            }
            password.isEmpty() -> {
                toast("Please enter your password")
                false
            }
            password.length < 6 -> {
                toast("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    private fun createAccount(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: run {
                        toast("User creation failed")
                        return@addOnCompleteListener
                    }

                    // Update user profile
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Save additional user data to Firestore
                                val userMap = hashMapOf(
                                    "fullName" to fullName,
                                    "email" to email,
                                    "createdAt" to System.currentTimeMillis()
                                )

                                firestore.collection("users").document(userId).set(userMap)
                                    .addOnSuccessListener {
                                        sendEmailVerification(user)
                                    }
                                    .addOnFailureListener { e ->
                                        toast("Failed to save data: ${e.message}")
                                        // Optional: Delete user if Firestore fails
                                        user.delete()
                                    }
                            } else {
                                toast("Failed to update profile: ${profileTask.exception?.message}")
                            }
                        }
                } else {
                    toast("Sign up failed: ${task.exception?.message}")
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Verification email sent to ${user.email}")
                } else {
                    toast("Failed to send verification email: ${task.exception?.message}")
                }
                // Navigate regardless of verification email success
                navigateToNextScreen()
            }
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this, DetectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}