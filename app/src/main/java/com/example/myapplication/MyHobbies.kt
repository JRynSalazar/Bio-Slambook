package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityAboutMeBinding
import com.example.myapplication.databinding.ActivityMyHobbiesBinding

class MyHobbies : AppCompatActivity() {
    private lateinit var binding: ActivityMyHobbiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyHobbiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("myHobbiesPreference", MODE_PRIVATE)
        loadData(sharedPreferences)

        binding.edit.setOnClickListener{
            val toEdit = Intent(this, EditMyHobbies::class.java)
            startActivity(toEdit)
            finish()
        }

        binding.back.setOnClickListener{
            val back = Intent(this, DashBoardViewer::class.java)
            startActivity(back)
            finish()
        }
    }

    private fun loadData(sharedPreferences: SharedPreferences) {
        val favHobby = sharedPreferences.getString("favHobby", "None")
        val indoor = sharedPreferences.getString("indoor", "None")
        val skill = sharedPreferences.getString("skill", "None")
        val newHobby = sharedPreferences.getString("newHobby", "None")
        val reading = sharedPreferences.getString("reading", "None")
        val music = sharedPreferences.getString("music", "None")
        val cook = sharedPreferences.getString("cook", "None")
        val created = sharedPreferences.getString("created", "None")
        val movies = sharedPreferences.getString("movies", "None")
        val travel = sharedPreferences.getString("travel", "None")



        binding.favHobby.text = favHobby
        binding.indoor.text = indoor
        binding.skill.text = skill
        binding.newHobby.text = newHobby
        binding.reading.text = reading
        binding.music.text = music
        binding.cook.text = cook
        binding.created.text = created
        binding.movies.text = movies
        binding.travel.text = travel

    }
}