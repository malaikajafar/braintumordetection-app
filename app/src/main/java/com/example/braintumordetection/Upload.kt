package com.example.braintumordetection

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var chooseFileButton: Button
    private lateinit var selectedFileName: TextView
    private lateinit var imagePreview: ImageView
    private lateinit var analyzeButton: Button

    private var selectedUri: Uri? = null

    companion object {
        const val FILE_SELECT_CODE = 1001
    }

    private val storageRef by lazy { FirebaseStorage.getInstance().reference }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)

        chooseFileButton = findViewById(R.id.chooseFileButton)
        selectedFileName = findViewById(R.id.selectedFileName)
        imagePreview = findViewById(R.id.imagePreview)
        analyzeButton = findViewById(R.id.analyzeScanButton)

        chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select MRI Scan"), FILE_SELECT_CODE)
        }

        analyzeButton.setOnClickListener {
            if (selectedUri != null) {
                uploadImageToFirebase(selectedUri!!)
            } else {
                Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            selectedUri = data?.data
            if (selectedUri != null) {
                imagePreview.setImageURI(selectedUri)
                imagePreview.visibility = View.VISIBLE

                // Show file name
                val fileName = getFileNameFromUri(selectedUri!!)
                selectedFileName.text = "Selected: $fileName"
                selectedFileName.visibility = View.VISIBLE
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storageRef.child("uploads/$fileName")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveImageInfoToFirestore(downloadUrl.toString())
                    progressDialog.dismiss()

                    Toast.makeText(this, "Upload successful!", Toast.LENGTH_SHORT).show()

                    // Go to result screen after upload
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("imageUri", downloadUrl.toString())  // better to pass download URL here
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageInfoToFirestore(downloadUrl: String) {
        val userId = auth.currentUser?.uid ?: "unknown_user"
        val data = hashMapOf(
            "userId" to userId,
            "imageUrl" to downloadUrl,
            "timestamp" to System.currentTimeMillis(),
            "result" to "Pending" // update later from ResultActivity if needed
        )

        firestore.collection("detections").add(data)
            .addOnSuccessListener {
                // Optional: You can log or Toast success here
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Helper function to get file name from Uri
    private fun getFileNameFromUri(uri: Uri): String {
        var result = "unknown"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex("_display_name")
            if (cursor.moveToFirst() && nameIndex >= 0) {
                result = cursor.getString(nameIndex)
            }
        }
        return result
    }
}
