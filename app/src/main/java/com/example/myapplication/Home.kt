package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.databinding.WellDoneBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.Manifest
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Base64

class Home : AppCompatActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val STORAGE_PERMISSION_REQUEST_CODE = 101

    private lateinit var binding: ActivityHomeBinding
    private var profilePicture: ByteArray? = null

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        val isHomeShown = sharedPreferences.getBoolean("isHomeShown", false)

        if (isHomeShown) {
            navigateToChooseType()
        } else {
            showHomeActivity(sharedPreferences)
        }
    }
    private fun showHomeActivity(sharedPreferences: SharedPreferences) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE)
            }
        }
        val items = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val adapter = CustomSpinnerAdapter(this, items)
        binding.customSpinner.adapter = adapter

        binding.customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = items[position]
                Toast.makeText(this@Home, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        binding.uploadImg.setOnClickListener {
            openImagePicker()
        }

        binding.doneButton.setOnClickListener {
            val name = binding.username.text.toString()
            val year = binding.year.text.toString()
            val month = binding.customSpinner.selectedItem.toString()
            val day = binding.day.text.toString()
            val gender = binding.genderC.selectedItem.toString()

            if(name.isEmpty() || year.isEmpty() || month.isEmpty() || day.isEmpty() || gender.isEmpty()){
                Toast.makeText(this, "Please fill up this the fields with *", Toast.LENGTH_SHORT).show()
            }else {
                saveData(sharedPreferences)
                showWellDonePopup()
            }
        }

        loadData(sharedPreferences)

        sharedPreferences.edit().putBoolean("isHomeShown", true).apply()
    }


    private fun navigateToChooseType() {
        val intent = Intent(this, ChooseType::class.java)
        startActivity(intent)
        finish()
    }

    // Function to open image picker dialog
    private fun openImagePicker() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> takePhoto()  // Open camera
                1 -> pickImageFromGallery()  // Open gallery
            }
        }
        builder.show()
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
        }
    }

    private fun pickImageFromGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    val inputStream: InputStream? = selectedImageUri?.let { contentResolver.openInputStream(it) }
                    profilePicture = inputStream?.readBytes()
                    inputStream?.close()

                    Glide.with(this)
                        .load(selectedImageUri)
                        .into(binding.profile)
                }
                TAKE_PHOTO_REQUEST -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    profilePicture = bitmapToByteArray(photo)

                    Glide.with(this)
                        .load(photo)
                        .into(binding.profile)
                }
            }
            Toast.makeText(this, "Profile picture set!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


    private fun showWellDonePopup() {
        val popupBinding = WellDoneBinding.inflate(layoutInflater)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_check)
            .into(popupBinding.gif)

        val mediaPlayer = MediaPlayer.create(this, R.raw.ding)
        mediaPlayer.start()

        val dialog = AlertDialog.Builder(this)
            .setView(popupBinding.root)
            .setCancelable(false)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                navigateToNextPage()
        }, 4500)
    }

    private fun navigateToNextPage() {
        val intent = Intent(this, ChooseType::class.java)
        startActivity(intent)
        finish()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }

    private fun saveData(sharedPreferences: SharedPreferences) {


        val name = binding.username.text.toString()
        val gender = binding.genderC.selectedItem.toString()
        val month = binding.customSpinner.selectedItem.toString()
        val day = binding.day.text.toString()
        val year = binding.year.text.toString()
        val address = binding.Address.text.toString()
        val contact = binding.Contact.text.toString()

        if (name.isEmpty() || day.isEmpty() || year.isEmpty() ||
            address.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all Fields!", Toast.LENGTH_SHORT).show()
            return
        }
        val editor  = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("gender", gender)
        editor.putString("month", month)
        editor.putString("day", day)
        editor.putString("year", year)
        editor.putString("address", address)
        editor.putString("contact", contact)

        if (profilePicture != null) {
            val base64Image = Base64.encodeToString(profilePicture, Base64.DEFAULT)
            editor.putString("profile_picture", base64Image)
        }

        editor.apply()
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()

    }
    private fun loadData(sharedPreferences: SharedPreferences) {
        binding.username.setText(sharedPreferences.getString("name", ""))
        binding.day.setText(sharedPreferences.getString("day", ""))
        binding.year.setText(sharedPreferences.getString("year", ""))
        binding.Address.setText(sharedPreferences.getString("address", ""))
        binding.Contact.setText(sharedPreferences.getString("contact", ""))

        val base64Image = sharedPreferences.getString("profile_picture", null)
        if (base64Image != null) {
            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Glide.with(this).load(bitmap).into(binding.profile)
        }
    }

}
