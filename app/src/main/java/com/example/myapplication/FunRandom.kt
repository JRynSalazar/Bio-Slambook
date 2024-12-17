package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFunRandomBinding


class FunRandom : AppCompatActivity() {

    private lateinit var binding: ActivityFunRandomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFunRandomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("funRandomPreference", MODE_PRIVATE)

        loadData(sharedPreferences)

        binding.back.setOnClickListener{
            val back = Intent(this, DashBoardViewer::class.java)
            startActivity(back)
            finish()
        }

        binding.edit.setOnClickListener{
            val toEdit = Intent(this, EditFunRandom::class.java)
            startActivity(toEdit)
        }

    }

    private fun loadData(sharedPreferences: SharedPreferences) {
        binding.pet.setText(sharedPreferences.getString("pet", ""))
        binding.crazy.setText(sharedPreferences.getString("crazy", ""))
        binding.superpower.setText(sharedPreferences.getString("superpower", ""))
        binding.moment.setText(sharedPreferences.getString("moment", ""))
        binding.stranded.setText(sharedPreferences.getString("stranded", ""))
        binding.food.setText(sharedPreferences.getString("food", ""))
        binding.time.setText(sharedPreferences.getString("time", ""))
        binding.dreamWeird.setText(sharedPreferences.getString("dreamWeird", ""))
        binding.lottery.setText(sharedPreferences.getString("lottery", ""))
        binding.swapLives.setText(sharedPreferences.getString("swapLives", ""))

    }

}