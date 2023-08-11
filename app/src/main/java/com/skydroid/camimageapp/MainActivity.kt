package com.skydroid.camimageapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var pickImage: FloatingActionButton
    private lateinit var pickFile: FloatingActionButton
    private lateinit var postApi: FloatingActionButton
    private lateinit var selectedImage: AppCompatImageView
    private lateinit var pickText: TextView
    private lateinit var pickPath: TextView
    private lateinit var progress_Bar_loanUser: ProgressBar

    companion object {
        private const val READ_EXTERNAL_PERMISSION = 101
    }

    lateinit var uriData: Uri
    var sPath  = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkCallPermission()
        pickImage = findViewById(R.id.pick_image)
        selectedImage = findViewById(R.id.selected_image)
        pickFile = findViewById(R.id.pick_file)
        pickText = findViewById(R.id.pick_text_data)
        pickPath = findViewById(R.id.pick_path_data)
        postApi = findViewById(R.id.postApi)
        progress_Bar_loanUser = findViewById(R.id.progress_Bar_loanUser)

        pickImage.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        pickFile.setOnClickListener {
            selectPDF()
        }

        postApi.setOnClickListener {
            if (selectedImage.drawable != null  || !sPath.equals("")){
                jsonParse()
            }else{
                Toast.makeText(applicationContext, "Please Choose File First...", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                uriData = data?.data!!
                selectedImage.setImageURI(uriData)
            }
        }

    private val changePdf =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                if (data != null) {
                    uriData = data.data!!
                    pickText.setText(Html.fromHtml("<big><b>PDF Uri</b></big><br>" + uriData))

                    // Get PDF path
                    sPath = uriData!!.path!!
                    pickPath.setText(Html.fromHtml(("<big><b>PDF Path</b></big><br>" + sPath)))
                }
            }

        }

    private fun selectPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        changePdf.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
        if (requestCode == READ_EXTERNAL_PERMISSION && grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            selectPDF()
        } else {
            Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCallPermission() {
        checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            READ_EXTERNAL_PERMISSION
        )

    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else { }
    }

    private fun jsonParse() {
        progress_Bar_loanUser.visibility = View.VISIBLE
        var farmerId = "41679719-f7b8-4ead-a27b-2060467e5ccc"
        var filePath = File(copyFileToInternalStorage(this,uriData,"test").toString().toUri().path.toString())
        val fileBody1: RequestBody =
            filePath.asRequestBody("mutlipart/form-data".toMediaTypeOrNull())
        var mediaImage = MultipartBody.Part.createFormData("files", filePath.name, fileBody1)
        val rewBody1: RequestBody =
            "Ex aadhar number".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val rewBody2: RequestBody =
            "image".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val rewBody3: RequestBody =
            "kyc".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val rewBody4: RequestBody =
            "aadhar".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        ApiRepo().postDocumentData(
            farmerId,
            ApiConstant.API_TOKEN,
            rewBody1,
            rewBody2,
            rewBody3,
            rewBody4,
            mediaImage
        )
            .enqueue(object : Callback<PostDocumentResponse?> {
                override fun onResponse(
                    call: Call<PostDocumentResponse?>,
                    response: Response<PostDocumentResponse?>
                ) {
                    if (response.isSuccessful) {
                        progress_Bar_loanUser.visibility = View.GONE
                        if (response.code() == 201) {
                            Log.e("#apihit","pass")
                            Toast.makeText(applicationContext, ""+response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<PostDocumentResponse?>, t: Throwable) {
                    Log.e("#apihit", t.message.toString())
                    progress_Bar_loanUser.visibility = View.GONE
                }
            })
    }

    fun copyFileToInternalStorage(context: Context, uri: Uri, newDirName: String): String? {
        val returnCursor = context.contentResolver.query(
            uri, arrayOf(
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
            ), null, null, null
        )

        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val output: File = if (newDirName != "") {
            val dir =
                File(context.filesDir.toString() + "/" + newDirName)
            if (!dir.exists()) {
                dir.mkdir()
            }
            File(context.filesDir.toString() + "/" + newDirName + "/" + name)
        } else {
            File(context.filesDir.toString() + "/" + name)
        }
        try {
            val inputStream =
                context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(output)
            var read = 0
            val bufferSize = 1024
            val buffers = ByteArray(bufferSize)
            while (inputStream!!.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return output.path
    }
}