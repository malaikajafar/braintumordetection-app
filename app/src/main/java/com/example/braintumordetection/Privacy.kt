package com.example.braintumordetection

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrivacySecurityActivity : AppCompatActivity() {

    private lateinit var switchEncryption: Switch
    private lateinit var switchTracking: Switch
    private lateinit var switch2FA: Switch
    private lateinit var switchAutoLogout: Switch
    private lateinit var btnSave: Button
    private lateinit var btnFeedback: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy)

        // Initialize views
        switchEncryption = findViewById(R.id.switchEncryption)
        switchTracking = findViewById(R.id.switchTracking)
        switch2FA = findViewById(R.id.switch2FA)
        switchAutoLogout = findViewById(R.id.switchAutoLogout)
        btnSave = findViewById(R.id.btnSave)
        btnFeedback = findViewById(R.id.btnFeedback)
        btnBack = findViewById(R.id.btnBack)

        // Save button logic
        btnSave.setOnClickListener {
            val encryption = switchEncryption.isChecked
            val tracking = switchTracking.isChecked
            val twoFA = switch2FA.isChecked
            val autoLogout = switchAutoLogout.isChecked

            // You could save these settings in SharedPreferences or a database
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }

        // Feedback button logic
        btnFeedback.setOnClickListener {
            Toast.makeText(this, "Feedback screen coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Back button logic
        btnBack.setOnClickListener {
            finish() // Simply go back to the previous screen
        }
    }
}
