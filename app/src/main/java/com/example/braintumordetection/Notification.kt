package com.example.braintumordetection

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NotificationsActivity : AppCompatActivity() {

    private lateinit var switchUpcomingAppointment: Switch
    private lateinit var switchTestResults: Switch
    private lateinit var switchMedicationReminder: Switch
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification)  // yahan apka XML ka file name dalen without .xml

        // Initialize views
        switchUpcomingAppointment = findViewById(R.id.switchUpcomingAppointment)
        switchTestResults = findViewById(R.id.switchTestResults)
        switchMedicationReminder = findViewById(R.id.switchMedicationReminder)
        btnBack = findViewById(R.id.btnBack)

        // Set listeners for switches
        switchUpcomingAppointment.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Upcoming Appointment notifications $status", Toast.LENGTH_SHORT).show()
        }

        switchTestResults.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Test Results notifications $status", Toast.LENGTH_SHORT).show()
        }

        switchMedicationReminder.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Medication Reminder notifications $status", Toast.LENGTH_SHORT).show()
        }

        // Back button click listener
        btnBack.setOnClickListener {
            finish()  // Close the activity and go back
        }
    }
}
