package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileSettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var switchPushNotifications: Switch
    private lateinit var btnLogout: Button
    private lateinit var btnEdit: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Firebase initialization
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        switchNotifications = findViewById(R.id.switchNotifications)
        switchPushNotifications = findViewById(R.id.switchPushNotifications)
        btnLogout = findViewById(R.id.btnLogout)
        btnEdit = findViewById(R.id.btnEdit)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            loadUserPreferences(currentUser.uid)
        }

        // Switch listeners
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            savePreference("notifications", isChecked)
            Toast.makeText(this, if (isChecked) "Notifications On" else "Notifications Off", Toast.LENGTH_SHORT).show()
        }

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            savePreference("push_notifications", isChecked)
            Toast.makeText(this, if (isChecked) "Push Notifications On" else "Push Notifications Off", Toast.LENGTH_SHORT).show()
        }

        // Logout action
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java)) // Replace with your login activity
            finish()
        }

        // Edit Profile action
        btnEdit.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    private fun savePreference(key: String, value: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(userId)

        val data = mapOf(key to value)
        userRef.set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                // Optionally show success message
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save preference", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserPreferences(userId: String) {
        val userRef = firestore.collection("users").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val notifications = document.getBoolean("notifications") ?: false
                    val pushNotifications = document.getBoolean("push_notifications") ?: false

                    switchNotifications.isChecked = notifications
                    switchPushNotifications.isChecked = pushNotifications
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load preferences", Toast.LENGTH_SHORT).show()
            }
    }
}
