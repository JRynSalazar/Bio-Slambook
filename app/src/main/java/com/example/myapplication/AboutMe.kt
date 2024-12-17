package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityAboutMeBinding
import com.example.myapplication.databinding.ActivityEditAboutMeBinding

class AboutMe : AppCompatActivity() {
    private lateinit var binding: ActivityAboutMeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("aboutMEPreference", MODE_PRIVATE)
        loadData(sharedPreferences)

        binding.edit.setOnClickListener{
            val toEdit = Intent(this, EditAboutMe::class.java)
            startActivity(toEdit)
        }

        binding.back.setOnClickListener{
            val back = Intent(this, DashBoardViewer::class.java)
            startActivity(back)
            finish()
        }


    }

    private fun loadData(sharedPreferences: SharedPreferences) {
        val nickname = sharedPreferences.getString("nickname", "None")
        val personality1 = sharedPreferences.getString("personality1", "None")
        val personality2 = sharedPreferences.getString("personality2", "None")
        val personality3 = sharedPreferences.getString("personality3", "None")
        val dream = sharedPreferences.getString("dream", "None")
        val motivates = sharedPreferences.getString("motivates", "None")
        val talent = sharedPreferences.getString("talent", "None")
        val motto = sharedPreferences.getString("motto", "None")
        val liveWithout1 = sharedPreferences.getString("liveWithout1", "None")
        val liveWithout2 = sharedPreferences.getString("liveWithout2", "None")
        val liveWithout3 = sharedPreferences.getString("liveWithout3", "None")


        binding.nickname.text = nickname
        binding.personality1.text = personality1
        binding.personality2.text = personality2
        binding.personality3.text = personality3
        binding.dream.text = dream
        binding.motivates.text = motivates
        binding.talent.text = talent
        binding.motto.text = motto
        binding.liveWithout1.text = liveWithout1
        binding.liveWithout2.text = liveWithout2
        binding.liveWithout3.text = liveWithout3

    }
}