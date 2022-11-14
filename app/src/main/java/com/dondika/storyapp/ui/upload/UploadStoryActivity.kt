package com.dondika.storyapp.ui.upload

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dondika.storyapp.R
import com.dondika.storyapp.databinding.ActivityUploadStoryBinding
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.UserViewModelFactory
import com.dondika.storyapp.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    private val uploadViewModel: UploadStoryViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    private lateinit var token: String
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null
    private var location: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(myFile.path)
            binding.imagePreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val selectedImage: Uri = it.data?.data as Uri
            val myFile = Utils.uriToFile(selectedImage, this@UploadStoryActivity)
            getFile = myFile
            binding.imagePreview.setImageURI(selectedImage)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getCurrentLocation()
            }
            else -> {
                //Snackbar.make()
                binding.switchLoc.isChecked = false
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(
                this@UploadStoryActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupListener()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION){
            if (!allPermissionGranted()){
                Toast.makeText(this, getString(R.string.upload_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupListener(){
        binding.buttonCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.buttonGallery.setOnClickListener {
            startGallery()
        }
        binding.buttonUpload.setOnClickListener {
            uploadStory()
        }
        binding.switchLoc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                getCurrentLocation()
            } else {
                this.location = null
            }
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        Utils.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "com.dondika.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadStory(){
        uploadViewModel.fetchUser().observe(this){ userToken ->
            //Log.e("cek", "tokenAct $userToken" )
            if (userToken != "")
                token = userToken
        }

        val inputDescription = binding.inputDesc
        var isValid = true
        if (inputDescription.text.toString().isBlank()){
            inputDescription.error = getString(R.string.upload_desc)
            isValid = false
        }
        if (getFile == null){
            Toast.makeText(this, getString(R.string.upload_image), Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (isValid){
            val file = reduceFileImage(getFile as File)
            val description = inputDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )

            var lat: RequestBody? = null
            var lon: RequestBody? = null

            if (location != null){
                lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
            }

            uploadViewModel.uploadStory(token, imageMultipart, description, lat, lon).observe(this){ uploadResponse ->
                when(uploadResponse){
                    is Result.Loading -> {
                        onLoading(true)
                    }
                    is Result.Success -> {
                        onLoading(false)
                        Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()

                        finish()
                    }
                    is Result.Error -> {
                        onLoading(false)
                        Toast.makeText(this, getString(R.string.upload_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun reduceFileImage(file: File): File{
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun onLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null){
                    this.location = currentLocation
                } else {
                    //Toast.makeText(, "", Toast.LENGTH_SHORT).show()
                    binding.switchLoc.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }


}





















