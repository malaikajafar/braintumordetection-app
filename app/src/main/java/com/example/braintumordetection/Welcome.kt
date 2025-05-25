package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.braintumordetection.R
import com.example.braintumordetection.LoginActivity

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // XML layout set karo
        setContentView(R.layout.welcomescreen)

        // Button ko find karo
        val getStartedButton: Button = findViewById(R.id.getStartedButton)

        // Button click listener
        getStartedButton.setOnClickListener {
            // Intent ke zariye dusri activity pe jao (example: DashboardActivity)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
