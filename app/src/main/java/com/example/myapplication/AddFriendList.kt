package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityAddFriendListBinding


class AddFriendList : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendListBinding
    private var friend: Friend? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        friend = intent.getParcelableExtra("friend_data")
       loadFriendData()

        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun loadFriendData() {
        friend?.let {
            binding.fullName.setText(it.name)
            binding.birthday.setText(it.birthday)
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
}