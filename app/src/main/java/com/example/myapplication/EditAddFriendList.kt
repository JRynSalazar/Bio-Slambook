package com.example.myapplication

import android.app.Activity
import android.content.Context
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
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditAddFriendListBinding
import com.example.myapplication.databinding.WellDoneBinding
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Calendar

class EditAddFriendList : AppCompatActivity() {

    private lateinit var binding: ActivityEditAddFriendListBinding
    private var profilePicture: ByteArray? = null

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2
    private var wellDoneDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAddFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cardView: CardView = binding.cardView14
        val scrollView: ScrollView = binding.scrollViewContent

        cardView.setOnClickListener {
            scrollView.visibility = if (scrollView.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        binding.saveSlam.setOnClickListener {
            scrollView.visibility = if (scrollView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        binding.back.setOnClickListener {
            val back = Intent(this, FriendDashBoard::class.java)
            startActivity(back)
            finish()
        }

        val monthItems = listOf(
            "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"
        )
        val monthAdapter = CustomSpinnerAdapter(this, monthItems)
        binding.customSpinner.adapter = monthAdapter

        binding.saveBtn.setOnClickListener {
            val name = binding.username.text.toString().trim()
            val yearText = binding.year.text.toString().trim()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            val year = yearText.toIntOrNull()
            if (name.isNotEmpty()) {
                if (year != null && year in 1924..currentYear) {
                   saveUserData()
                    showWellDonePopup {
                        val toFriend = Intent(this, FriendDashBoard::class.java)
                        startActivity(toFriend)
                        finish()
                        Toast.makeText(this, "$name is added successfully", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Invalid year", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                Toast.makeText(this, "Please Fill up your Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.uploadImg.setOnClickListener {
            openImagePicker()
        }
    }

    private fun saveUserData() {
        val name = binding.username.text.toString().trim()
        val month = binding.customSpinner.selectedItem.toString().trim()
        val day = binding.day.selectedItem.toString().trim()
        val year = binding.year.text.toString().trim()

        // Collect inputs from the questions
        val nickname = binding.question1.text.toString().trim()
        val biggestDream = binding.question2.text.toString().trim()
        val currentMoodSong = binding.question3.text.toString().trim()
        val motivation = binding.question4.text.toString().trim()
        val hiddenTalent = binding.question5.text.toString().trim()
        val favoriteQuote = binding.question6.text.toString().trim()
        val threeThings = binding.question7.text.toString().trim()
        val celebrityCrush = binding.question8.text.toString().trim()

        // Convert profile picture to Base64 string if it exists
        val profilePictureBase64 = profilePicture?.let { encodeImageToBase64(it) }

        // Create the Friend object
        val friend = Friend(
            profilePicture = profilePictureBase64,
            name = name,
            birthday = "$month $day, $year",
            nickname = nickname,
            biggestDream = biggestDream,
            currentMoodSong = currentMoodSong,
            motivation = motivation,
            hiddenTalent = hiddenTalent,
            favoriteQuote = favoriteQuote,
            threeThings = threeThings,
            celebrityCrush = celebrityCrush
        )

        // Save the friend data as a JSON string in SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val friendJson = gson.toJson(friend) // Serialize the Friend object to JSON
        editor.putString("user_data", friendJson)
        editor.apply()



        // Send the result back
       val resultIntent = Intent()
        resultIntent.putExtra("new_friend", friendJson)
        setResult(Activity.RESULT_OK, resultIntent)


        finish()

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
            .setCancelable(false)  // Ensure it can't be dismissed manually
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()


        onPopupComplete.invoke()


        dialog.setOnDismissListener {
            onPopupComplete.invoke()
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



    private fun resizeImage(uri: Uri, maxWidth: Int, maxHeight: Int): ByteArray {
        val inputStream = contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeStream(inputStream, null, options)

        var scaleFactor = 1
        while (options.outWidth / scaleFactor > maxWidth || options.outHeight / scaleFactor > maxHeight) {
            scaleFactor *= 2
        }

        val inputStreamForDecode = contentResolver.openInputStream(uri) // Reopen stream for actual decoding
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

    class CustomSpinnerAdapter(context: Context, items: List<String>) :
        ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items)
}