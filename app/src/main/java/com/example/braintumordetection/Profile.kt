package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileSettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var switchPushNotifications: Switch
    private lateinit var btnLogout: Button
    private lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile) // Make sure your XML file is named this

        // Initialize views
        switchNotifications = findViewById(R.id.switchNotifications)
        switchPushNotifications = findViewById(R.id.switchPushNotifications)
        btnLogout = findViewById(R.id.btnLogout)
        btnEdit = findViewById(R.id.btnEdit)

        // Switch listeners
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "Notifications On" else "Notifications Off", Toast.LENGTH_SHORT).show()
            // You can save this state in SharedPreferences or Firebase if needed
        }

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "Push Notifications On" else "Push Notifications Off", Toast.LENGTH_SHORT).show()
        }

        // Logout action
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            // TODO: Add FirebaseAuth.getInstance().signOut() if using Firebase
            // Navigate to login screen or finish activity
            finish()
        }

        // Edit Profile
        btnEdit.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            // TODO: Start EditProfileActivity or show edit dialog
        }
    }
}
