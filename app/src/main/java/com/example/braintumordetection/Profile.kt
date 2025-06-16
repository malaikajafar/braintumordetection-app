package com.example.braintumordetection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var switchUpcomingAppointment: Switch
    private lateinit var goToSecurityButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile) // Make sure this matches your XML file name

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        switchUpcomingAppointment = findViewById(R.id.switchUpcomingAppointment)
        goToSecurityButton = findViewById(R.id.goToSecurityButton)

        // Get SharedPreferences for switch state
        val sharedPrefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isNotificationEnabled = sharedPrefs.getBoolean("notifications_enabled", true)
        switchUpcomingAppointment.isChecked = isNotificationEnabled

        // Handle switch state changes
        switchUpcomingAppointment.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("notifications_enabled", isChecked).apply()
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Notifications $status", Toast.LENGTH_SHORT).show()
        }

        // Handle Logout button
        goToSecurityButton.setOnClickListener {
            auth.signOut() // Sign out from Firebase if used

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
