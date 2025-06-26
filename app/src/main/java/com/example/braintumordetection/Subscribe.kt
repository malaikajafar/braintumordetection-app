package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SubscribeActivity : AppCompatActivity() {

    private lateinit var subscribeButton: Button
    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subscribe) // Make sure XML filename matches

        auth = FirebaseAuth.getInstance()

        subscribeButton = findViewById(R.id.subscribeButton)
        logoutButton = findViewById(R.id.logoutButton)

        // 👉 Subscribe button logic (currently a placeholder)
        subscribeButton.setOnClickListener {
            Toast.makeText(this, "Subscription feature coming soon!", Toast.LENGTH_SHORT).show()

            // TODO: Replace with billing logic when subscription implemented
            // Example (if subscription done):
            // val intent = Intent(this, HomeActivity::class.java)
            // startActivity(intent)
            // finish()
        }

        // 🔐 Logout logic
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    // ⛔ Disable back button to prevent bypassing this screen
    override fun onBackPressed() {
        Toast.makeText(this, "Going back...", Toast.LENGTH_SHORT).show()
        super.onBackPressed() // Default back behavior
    }

}
