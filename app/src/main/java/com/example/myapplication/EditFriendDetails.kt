package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditFriendDetailsBinding
import com.example.myapplication.databinding.WellDoneBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar
import kotlin.String

class EditFriendDetails : AppCompatActivity() {

    private lateinit var binding: ActivityEditFriendDetailsBinding
    private var friend: Friend? = null
    private var profilePicture: ByteArray? = null
    private val handler = Handler(Looper.getMainLooper())
    private var wellDoneDialog: AlertDialog? = null


    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFriendDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friend = intent.getParcelableExtra("friend_data")
        loadFriendData()

        binding.username.setOnClickListener{
            binding.warningName.visibility = View.GONE
        }
        binding.year.setOnClickListener{
            binding.warningYear.visibility = View.GONE
        }


        val monthItems = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.customSpinner.adapter = adapter

        binding.customSpinner.setSelection(0)

        binding.saveBtn.setOnClickListener {
            val name = binding.username.text.toString().trim()
            val yearText = binding.year.text.toString().trim()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val year = yearText.toIntOrNull()
            val yearF = currentYear - 17

            if (name.isNotEmpty()) {
                if (year != null && year in 1924..yearF) {

                    saveFriendData()

                    showWellDonePopup()

                    navigateToDash()
                    Toast.makeText(this, "$name is added successfully", Toast.LENGTH_SHORT).show()

                } else {
                    binding.warningYear.visibility = View.VISIBLE
                    Toast.makeText(this, "Invalid year (minor isn't allowed)", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                binding.warningName.visibility = View.VISIBLE
                Toast.makeText(this, "Please Fill up your Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.uploadImg.setOnClickListener {
            openImagePicker()
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

        private fun loadFriendData() {
            friend?.let {
                val monthItems = listOf(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
                )
                val monthName = it.birthday?.split(" ")?.get(0)
                val monthIndex = monthItems.indexOf(monthName)
                binding.customSpinner.setSelection(monthIndex)


                val dayName = it.birthday?.split(" ")?.get(1)
                val dayIndex = monthItems.indexOf(dayName)

                binding.day.setSelection(dayIndex)

                val yearName = it.birthday?.split(" ")?.get(2)
                binding.year.setText(yearName)
                binding.username.setText(it.name)
                binding.question1.setText(it.nickname)
                binding.question2.setText(it.biggestDream)
                binding.question3.setText(it.currentMoodSong)
                binding.question4.setText(it.motivation)
                binding.question5.setText(it.hiddenTalent)
                binding.question6.setText(it.favoriteQuote)
                binding.question7.setText(it.threeThings)
                binding.question8.setText(it.celebrityCrush)
                it.profilePicture?.let { base64Image ->
                    val decodedImage = Base64.decode(base64Image, Base64.DEFAULT)
                    val decodedBitmap =
                        BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
                    Glide.with(this)
                        .load(decodedBitmap)
                        .into(binding.profile)
                }
            }
        }

    private fun saveFriendData() {
        val month = binding.customSpinner.selectedItem?.toString() ?: "January"
        val day = binding.day.selectedItem?.toString() ?: "1"
        val year = binding.year.text.toString()
        val profilePictureBase64 = profilePicture?.let { encodeImageToBase64(it) }

        if (year.isBlank()) {
            Toast.makeText(this, "Please enter a valid year", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedFriend = Friend(

            profilePicture = profilePictureBase64,
            name = binding.username.text.toString(),
            birthday = "$month $day, $year",
            nickname = binding.question1.text.toString(),
            biggestDream = binding.question2.text.toString(),
            currentMoodSong = binding.question3.text.toString(),
            motivation = binding.question4.text.toString(),
            hiddenTalent = binding.question5.text.toString(),
            favoriteQuote = binding.question6.text.toString(),
            threeThings = binding.question7.text.toString(),
            celebrityCrush = binding.question8.text.toString()
        )

        val intent = intent
        intent.putExtra("updated_friend", updatedFriend)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        try {

                            profilePicture = resizeImage(it, 1024, 1024)

                            Glide.with(this)
                                .load(it)
                                .into(binding.profile)

                            Toast.makeText(this, "Profile picture set!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this, "Failed to load image: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                TAKE_PHOTO_REQUEST -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    profilePicture = bitmapToByteArray(photo)

                    Glide.with(this)
                        .load(photo)
                        .into(binding.profile)

                    Toast.makeText(this, "Profile picture set!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun openImagePicker() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Profile Picture")
        builder.setItems(options) { _, which ->
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
    private fun resizeImage(uri: Uri, maxWidth: Int, maxHeight: Int): ByteArray {
        val inputStream = contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeStream(inputStream, null, options)

        var scaleFactor = 1
        while (options.outWidth / scaleFactor > maxWidth || options.outHeight / scaleFactor > maxHeight) {
            scaleFactor *= 2
        }

        val inputStreamForDecode = contentResolver.openInputStream(uri)
        val scaledOptions = BitmapFactory.Options().apply { inSampleSize = scaleFactor }
        val resizedBitmap = BitmapFactory.decodeStream(inputStreamForDecode, null, scaledOptions)

        val stream = ByteArrayOutputStream()
        resizedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }


    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    private fun encodeImageToBase64(imageBytes: ByteArray): String {
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
    private fun navigateToDash() {
        handler.postDelayed({
            val toFriend = Intent(this, FriendDashBoard::class.java)
            startActivity(toFriend)
            finishAffinity()
        }, 3000)
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

        wellDoneDialog = dialog

        handler.postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 4500)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Cancel all pending callbacks
        wellDoneDialog?.dismiss() // Dismiss dialog if still showing
    }
}
