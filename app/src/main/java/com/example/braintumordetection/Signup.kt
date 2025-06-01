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

        fullNameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signupButton)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        signUpButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // 🛡️ Input Validation
            when {
                fullName.isEmpty() -> showToast("Please enter your full name")
                email.isEmpty() -> showToast("Please enter your email")
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast("Invalid email format")
                password.isEmpty() -> showToast("Please enter your password")
                password.length < 6 -> showToast("Password must be at least 6 characters")
                else -> {
                    Log.d("SignUp", "Trying to sign up...")
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                val userMap = hashMapOf(
                                    "fullName" to fullName,
                                    "email" to email
                                )

                                firestore.collection("users").document(userId!!)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        Log.d("SignUp", "User data saved in Firestore")
                                        showToast("Sign Up Successful!")
                                        val intent = Intent(this, DetectionActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("SignUp", "Firestore error", e)
                                        showToast("Error saving user data: ${e.message}")
                                    }

                            } else {
                                Log.e("SignUp", "Firebase signup error", task.exception!!)
                                showToast("Sign Up Failed: ${task.exception?.message}")
                            }
                        }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
