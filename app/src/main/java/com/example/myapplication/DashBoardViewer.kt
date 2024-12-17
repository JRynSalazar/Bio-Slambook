package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDashBoardViewerBinding

class DashBoardViewer : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        loadData(sharedPreferences)

        binding.editProf.setOnClickListener{
            val toEdit = Intent(this, EditProfile::class.java)
            startActivity(toEdit)
            finish()
        }

        binding.aboutMe.setOnClickListener{
            val toAbout = Intent(this, AboutMe::class.java)
            startActivity(toAbout)
            finish()

        }

        binding.hobbies.setOnClickListener{
            val toHobbies = Intent(this, MyHobbies::class.java)
            startActivity(toHobbies)
            finish()
        }

        binding.love.setOnClickListener{
            val toLove = Intent(this, LoveRelationship::class.java)
            startActivity(toLove)
            finish()
        }

        binding.funRandom.setOnClickListener{
            val toRandom = Intent(this, FunRandom::class.java)
            startActivity(toRandom)
            finish()
        }

        binding.back.setOnClickListener{
            val back = Intent(this, ChooseType::class.java)
            startActivity(back)
            finish()
        }

    }



    private fun loadData(sharedPreferences: SharedPreferences) {
        val name = sharedPreferences.getString("name", "Unknown Name")
        val gender = sharedPreferences.getString("gender", "Unknown Gender")
        val month = sharedPreferences.getString("month", "Unknown")
        val day = sharedPreferences.getString("day", "Unknown")
        val year = sharedPreferences.getString("year", "Unknown")
        val address = sharedPreferences.getString("address", "Unknown Address")
        val contact = sharedPreferences.getString("contact", "Unknown Contact")
        val profileImageBase64 = sharedPreferences.getString("profile_picture", null)


        if (profileImageBase64 != null) {
            val imageBytes = Base64.decode(profileImageBase64, Base64.DEFAULT)
            Glide.with(this)
                .load(imageBytes)
                .into(binding.profileImg)
        } else {
            binding.profileImg.setImageResource(R.drawable.default_save) // Fallback image
        }


        binding.name.text = name
        binding.gender.text = gender
        binding.bday.text = "$month $day, $year"
        binding.address.text = address
        binding.contact.text = contact
    }
}
