package com.example.braintumordetection

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfileSettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var switchPushNotifications: Switch
    private lateinit var btnLogout: Button
    private lateinit var btnEdit: Button
    private lateinit var layoutChangePassword: LinearLayout
    private lateinit var layoutPrivacy: LinearLayout
    private lateinit var layoutHelpSupport: LinearLayout

    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private var isEditMode = false

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        switchNotifications = findViewById(R.id.switchNotifications)
        switchPushNotifications = findViewById(R.id.switchPushNotifications)
        btnLogout = findViewById(R.id.btnLogout)
        btnEdit = findViewById(R.id.btnEdit)
        editFullName = findViewById(R.id.editFullName)
        editEmail = findViewById(R.id.editEmail)

        layoutChangePassword = findViewById(R.id.btnChangePassword)
        layoutPrivacy = findViewById(R.id.btnPrivacy)
        layoutHelpSupport = findViewById(R.id.btnHelpSupport)

        // Disable fields by default
        editFullName.isEnabled = false
        editEmail.isEnabled = false

        val userId = auth.currentUser?.uid
        userId?.let {
            loadUserPreferences(it)
            loadUserProfile(it)
        }

        // Notification switches
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            savePreference("notifications", isChecked)
            toast(if (isChecked) "Notifications On" else "Notifications Off")
        }

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            savePreference("push_notifications", isChecked)
            toast(if (isChecked) "Push Notifications On" else "Push Notifications Off")
        }

        // Logout
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Edit/Save toggle
        btnEdit.setOnClickListener {
            isEditMode = !isEditMode

            if (isEditMode) {
                editFullName.isEnabled = true
                editEmail.isEnabled = true
                btnEdit.text = "Save"
                toast("You can now edit your profile")
            } else {
                val newName = editFullName.text.toString().trim()
                val newEmail = editEmail.text.toString().trim()

                if (newName.isEmpty() || newEmail.isEmpty()) {
                    toast("Fields cannot be empty")
                    return@setOnClickListener
                }

                val updates = hashMapOf(
                    "fullName" to newName,
                    "email" to newEmail
                )

                firestore.collection("profile").document(userId!!).update(updates as Map<String, Any>)
                    .addOnSuccessListener {
                        toast("Profile updated successfully")
                        editFullName.isEnabled = false
                        editEmail.isEnabled = false
                        btnEdit.text = "Edit"
                    }
                    .addOnFailureListener { e ->
                        toast("Failed to update profile: ${e.message}")
                    }
            }
        }

        // Change Password Dialog
        layoutChangePassword.setOnClickListener {
            val editText = EditText(this)
            editText.hint = "Enter new password"
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(editText)
                .setPositiveButton("Change") { _, _ ->
                    val newPassword = editText.text.toString().trim()
                    if (newPassword.length < 6) {
                        toast("Password must be at least 6 characters")
                        return@setPositiveButton
                    }

                    auth.currentUser?.updatePassword(newPassword)
                        ?.addOnSuccessListener {
                            toast("Password changed successfully")
                        }
                        ?.addOnFailureListener {
                            toast("Failed to change password: ${it.message}")
                        }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }

        // Help & Support Dialog
        layoutHelpSupport.setOnClickListener {
            val helpMessage = """
                • For technical support, contact: support@example.com
                • Make sure your app is up to date
                • Visit our FAQ section for more help
                • Restart the app if you're facing issues
            """.trimIndent()

            android.app.AlertDialog.Builder(this)
                .setTitle("Help & Support")
                .setMessage(helpMessage)
                .setPositiveButton("OK", null)
                .show()
        }

        // Privacy opens a new activity
        layoutPrivacy.setOnClickListener {
            val intent = Intent(this, PrivacySecurityActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserPreferences(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    switchNotifications.isChecked = doc.getBoolean("notifications") ?: false
                    switchPushNotifications.isChecked = doc.getBoolean("push_notifications") ?: false
                }
            }
            .addOnFailureListener {
                toast("Failed to load preferences")
            }
    }

    private fun loadUserProfile(userId: String) {
        firestore.collection("profile").document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    editFullName.setText(doc.getString("fullName") ?: "")
                    editEmail.setText(doc.getString("email") ?: "")
                }
            }
            .addOnFailureListener {
                toast("Failed to load profile")
            }
    }

    private fun savePreference(key: String, value: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(userId)

        val data = mapOf(key to value)
        userRef.set(data, SetOptions.merge())
            .addOnFailureListener {
                toast("Failed to save preference")
            }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
