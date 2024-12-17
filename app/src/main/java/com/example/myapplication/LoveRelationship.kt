package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoveRelationshipBinding


class LoveRelationship : AppCompatActivity() {

    private lateinit var binding: ActivityLoveRelationshipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoveRelationshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("lovePreference", MODE_PRIVATE)

        loadData(sharedPreferences)

        binding.back.setOnClickListener{
            val back = Intent(this, DashBoardViewer::class.java)
            startActivity(back)
            finish()
        }

        binding.edit.setOnClickListener{
            val toEdit = Intent(this, EditLoveRelationship::class.java)
            startActivity(toEdit)
        }
    }

    private fun loadData(sharedPreferences: SharedPreferences) {
        binding.loveMean.setText(sharedPreferences.getString("loveMean", ""))
        binding.relationship.setText(sharedPreferences.getString("relationship", ""))
        binding.date.setText(sharedPreferences.getString("date", ""))
        binding.partner.setText(sharedPreferences.getString("partner", ""))
        binding.show.setText(sharedPreferences.getString("show", ""))
        binding.soulmates.setText(sharedPreferences.getString("soulmates", ""))
        binding.sweetest.setText(sharedPreferences.getString("sweetest", ""))
        binding.conflicts.setText(sharedPreferences.getString("conflicts", ""))
        binding.learned.setText(sharedPreferences.getString("learned", ""))
        binding.advice.setText(sharedPreferences.getString("advice", ""))
    }
}