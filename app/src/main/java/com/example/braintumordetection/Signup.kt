package com.example.braintumordetection // Change to your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up) // XML file name must be activity_sign_up.xml

        // Initialize views
        fullNameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signupButton)

        // Handle sign-up button click
        signUpButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Basic input validation
            if (fullName.isEmpty()) {
                showToast("Please enter your full name")
            } else if (email.isEmpty()) {
                showToast("Please enter your email")
            } else if (password.isEmpty()) {
                showToast("Please enter your password")
            } else {
                // Here you can add actual sign-up logic (e.g., Firebase, local DB, etc.)
                showToast("Sign Up Successful!")


                val intent = Intent(this, DetectionActivity::class.java)
                startActivity(intent)


                finish()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
