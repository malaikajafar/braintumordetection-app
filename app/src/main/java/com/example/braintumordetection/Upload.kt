package com.example.braintumordetection

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class UploadActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var chooseFileButton: Button
    private lateinit var analyzeScanButton: Button
    private lateinit var selectedFileName: TextView
    private lateinit var imagePreview: ImageView
    // Removed: private lateinit var resultTextView: TextView

    private var imageUri: Uri? = null
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)

        chooseFileButton = findViewById(R.id.chooseFileButton)
        analyzeScanButton = findViewById(R.id.analyzeScanButton)
        selectedFileName = findViewById(R.id.selectedFileName)
        imagePreview = findViewById(R.id.imagePreview)
        // Removed initialization of resultTextView

        analyzeScanButton.isEnabled = false  // Disable analyze button until image selected

        chooseFileButton.setOnClickListener {
            openFileChooser()
        }

        analyzeScanButton.setOnClickListener {
            if (imagePath != null) {
                sendImageToFlask(imagePath!!)
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imagePreview.setImageURI(imageUri)
            imagePreview.visibility = View.VISIBLE
            imagePath = getRealPathFromURI(imageUri!!)
            if (imagePath != null) {
                selectedFileName.text = File(imagePath!!).name
                Log.d("UploadActivity", "Selected image path: $imagePath")
                analyzeScanButton.isEnabled = true  // Enable analyze button now
            } else {
                Toast.makeText(this, "Unable to get image path", Toast.LENGTH_SHORT).show()
                analyzeScanButton.isEnabled = false
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        return if (cursor != null) {
            cursor.moveToFirst()
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val path = cursor.getString(index)
            cursor.close()
            path
        } else null
    }

    private fun sendImageToFlask(imagePath: String) {
        val file = File(imagePath)

        if (!file.exists()) {
            runOnUiThread {
                Toast.makeText(this, "File does not exist: $imagePath", Toast.LENGTH_LONG).show()
                // Removed: resultTextView.text = ""
            }
            return
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", file.name, file.asRequestBody("image/jpeg".toMediaType()))
            .build()

        val request = Request.Builder()
            .url("http://192.168.0.104:5000/predict")  // Update with your Flask server IP and port
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.e("API_ERROR", "Network error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@UploadActivity, "Connection Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    // Removed: resultTextView.text = ""
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val result = response.body?.string()
                    Log.d("API_RESPONSE", "Response: $result")

                    runOnUiThread {
                        if (response.isSuccessful && result != null) {
                            try {
                                val json = JSONObject(result)
                                val predictedClass = json.optString("class_", "N/A")
                                val confidence = json.optDouble("confidence", 0.0)
                                val advice = json.optString("advice", "No advice available.")

                                // Removed: showing text on resultTextView

                                // Start ResultActivity with data
                                val intent = Intent(this@UploadActivity, ResultActivity::class.java)
                                intent.putExtra("predictionResult", predictedClass)
                                intent.putExtra("confidence", confidence)
                                intent.putExtra("advice", advice)
                                startActivity(intent)

                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(this@UploadActivity, "Failed to parse response.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(
                                this@UploadActivity,
                                "Failed to get valid response from server",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("UploadActivity", "Error in onResponse: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this@UploadActivity, "Error processing response.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
