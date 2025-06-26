package com.example.braintumordetection

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)

        chooseFileButton = findViewById(R.id.chooseFileButton)
        selectedFileName = findViewById(R.id.selectedFileName)
        imagePreview = findViewById(R.id.imagePreview)
        analyzeButton = findViewById(R.id.analyzeScanButton)

        chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(Intent.createChooser(intent, "Select MRI Scan"), FILE_SELECT_CODE)
        }

        analyzeButton.setOnClickListener {
            selectedUri?.let {
                simulateImageProcessing(it)
            } ?: Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            selectedUri = data?.data
            selectedUri?.let {
                imagePreview.setImageURI(it)
                imagePreview.visibility = View.VISIBLE

                val fileName = getFileNameFromUri(it)
                selectedFileName.text = "Selected: $fileName"
                selectedFileName.visibility = View.VISIBLE
            }
        }
    }

    private fun simulateImageProcessing(imageUri: Uri) {
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Analyzing...")
            setCancelable(false)
            show()
        }

        // Simulate delay (e.g., for local ML model in future)
        imagePreview.postDelayed({
            progressDialog.dismiss()

            val predictionResult = "Tumor" // 🔮 Placeholder for local ML result

            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("imageUri", imageUri.toString())
                putExtra("predictionResult", predictionResult)
            }
            startActivity(intent)

            Toast.makeText(this, "Analysis complete!", Toast.LENGTH_SHORT).show()
        }, 2000) // 2 seconds delay
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var result = "unknown_file.jpg"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                result = cursor.getString(nameIndex)
            }
        }
        return result
    }
}
