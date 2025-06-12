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

        // View bindings
        fullNameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signupButton)

        // Firebase initialization
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Sign Up Click Listener
        signUpButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (!validateInputs(fullName, email, password)) return@setOnClickListener

            createFirebaseUser(fullName, email, password)
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

    private fun createFirebaseUser(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userMap = hashMapOf(
                            "fullName" to fullName,
                            "email" to email
                        )

                        firestore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Log.d("SignUp", "User data saved in Firestore")
                                showToast("Sign Up Successful!")
                                startActivity(Intent(this, DetectionActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e("SignUp", "Firestore error", e)
                                showToast("Error saving user data: ${e.message}")
                            }
                    } else {
                        showToast("Unexpected error: User ID is null")
                        Log.e("SignUp", "FirebaseAuth userId is null")
                    }
                } else {
                    val errorMsg = task.exception?.message ?: "Unknown error"
                    Log.e("SignUp", "Firebase signup error: $errorMsg", task.exception)
                    showToast("Sign Up Failed: $errorMsg")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
