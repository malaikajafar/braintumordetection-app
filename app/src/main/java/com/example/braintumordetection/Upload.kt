package com.example.braintumordetection

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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

        // Initialization
        chooseFileButton = findViewById(R.id.chooseFileButton)
        selectedFileName = findViewById(R.id.selectedFileName)
        imagePreview = findViewById(R.id.imagePreview)
        analyzeButton = findViewById(R.id.analyzeScanButton)

        // File choose logic
        chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select MRI Scan"), FILE_SELECT_CODE)
        }

        // Analyze Scan button click logic
        analyzeButton.setOnClickListener {
            if (selectedUri != null) {
                val intent = Intent(this,ResultActivity::class.java)
                intent.putExtra("imageUri", selectedUri.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle selected image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            selectedUri = data?.data
            if (selectedUri != null) {
                imagePreview.setImageURI(selectedUri)
                imagePreview.visibility = View.VISIBLE
                selectedFileName.visibility = View.GONE
            }
        }
    }
}
