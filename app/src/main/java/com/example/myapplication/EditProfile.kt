package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditProfileBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.provider.MediaStore
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.WellDoneBinding
import java.util.Calendar

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var profilePicture: ByteArray? = null

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)


        loadData(sharedPreferences)

        val genderItems = listOf("Male", "Female", "LGBTQ")
        val genderAdapter = CustomSpinnerAdapter(this, genderItems)
        binding.genderC.adapter = genderAdapter

        // Handle month spinner selection
        val monthItems = listOf(
            "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"
        )
        val monthAdapter = CustomSpinnerAdapter(this, monthItems)
        binding.customSpinner.adapter = monthAdapter


        binding.uploadImg.setOnClickListener {
            openImagePicker()
        }

        binding.username.setOnClickListener{
            binding.warningName.visibility = View.GONE
        }
        binding.day.setOnClickListener{
            binding.warningDay.visibility = View.GONE
        }
        binding.year.setOnClickListener{
            binding.warningYear.visibility = View.GONE
        }
        binding.Address.setOnClickListener{
            binding.warningAdd.visibility = View.GONE
        }


        binding.doneButton.setOnClickListener {
            saveData(sharedPreferences)


        }
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

    // Function to save data to SharedPreferences
    private fun saveData(sharedPreferences: SharedPreferences) {
        val name = binding.username.text.toString().trim()
        val gender = binding.genderC.selectedItem.toString()
        val month = binding.customSpinner.selectedItem.toString()  // Get selected month
        val day = binding.day.text.toString().trim()
        val dayInt = day.toIntOrNull()
        val year = binding.year.text.toString().trim()
        val yearInt = year.toIntOrNull()
        val address = binding.Address.text.toString().trim()
        val contact = binding.Contact.text.toString()
        val currentYr = Calendar.getInstance().get(Calendar.YEAR)
        val yearF = currentYr - 17


        if (name.isEmpty()) {
            binding.warningName.visibility = View.VISIBLE
            return
        } else if (address.isEmpty()) {
            binding.warningAdd.visibility = View.VISIBLE
            return
        } else if (day.isEmpty()) {
            binding.warningDay.visibility = View.VISIBLE
            return
        } else if (year.isEmpty()) {
            binding.warningYear.visibility = View.VISIBLE
            return
        } else {
            if (dayInt !in 1..31) {
                Toast.makeText(this, "Invalid Input: Please Enter Valid Day", Toast.LENGTH_SHORT)
                    .show()
                binding.warningDay.visibility = View.VISIBLE
                return
            }else if(yearInt !in 1934..yearF) {
                Toast.makeText(this, "Invalid Input: Please Enter Valid Year(only age 18 above )", Toast.LENGTH_SHORT)
                    .show()
                binding.warningYear.visibility = View.VISIBLE
                return
            }
            val editor = sharedPreferences.edit()
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
            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
        }

        showWellDonePopup {
            val toDash = Intent(this, DashBoardViewer::class.java)
            startActivity(toDash)
            finish()
        }

    }
    // Function to load data from SharedPreferences
    private fun loadData(sharedPreferences: SharedPreferences) {
        binding.username.setText(sharedPreferences.getString("name", ""))
        binding.Address.setText(sharedPreferences.getString("address", ""))
        binding.Contact.setText(sharedPreferences.getString("contact", ""))
        binding.year.setText(sharedPreferences.getString("year", ""))
        binding.day.setText(sharedPreferences.getString("day", ""))

        val gender = sharedPreferences.getString("gender", "")
        val genderPosition = when (gender) {
            "Male" -> 0
            "Female" -> 1
            "LGBTQ" -> 2
            else -> 0
        }
        binding.genderC.setSelection(genderPosition)

        // Load selected month
        val month = sharedPreferences.getString("month", "January")
        val monthPosition = when (month) {
            "January" -> 0
            "February" -> 1
            "March" -> 2
            "April" -> 3
            "May" -> 4
            "June" -> 5
            "July" -> 6
            "August" -> 7
            "September" -> 8
            "October" -> 9
            "November" -> 10
            "December" -> 11
            else -> 0
        }
        binding.customSpinner.setSelection(monthPosition)

        val base64Image = sharedPreferences.getString("profile_picture", null)
        if (base64Image != null) {
            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Glide.with(this).load(bitmap).into(binding.profile)
        }
    }
    private fun showWellDonePopup(onPopupComplete: () -> Unit) {
        val popupBinding = WellDoneBinding.inflate(layoutInflater)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_check)
            .into(popupBinding.gif)

        val mediaPlayer = MediaPlayer.create(this, R.raw.ding)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
        mediaPlayer.start()

        val dialog = AlertDialog.Builder(this)
            .setView(popupBinding.root)
            .setCancelable(false)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
            onPopupComplete()
        }, 4000)
    }
}
