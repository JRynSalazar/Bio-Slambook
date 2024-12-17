package com.example.myapplication

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityChooseTypeBinding

class ChooseType : AppCompatActivity() {
    private lateinit var binding: ActivityChooseTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityChooseTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.cardContent.layoutTransition = LayoutTransition()
        binding.cardContent1.layoutTransition = LayoutTransition()


        binding.expandableCard.setOnClickListener {
            val visibility = if (binding.cardText.visibility == View.GONE) View.VISIBLE else View.GONE
            binding.cardText.visibility = visibility
            binding.cardContent.visibility = visibility
        }


        binding.expandableCard1.setOnClickListener {
            val visibility = if (binding.cardText1.visibility == View.GONE) View.VISIBLE else View.GONE
            binding.cardText1.visibility = visibility
            binding.cardContent1.visibility = visibility
        }


        binding.navigateButton.setOnClickListener {
            val toDash = Intent(this, DashBoardViewer::class.java)
            startActivity(toDash)

        }

        // Navigate button for Friend Slambook
        binding.navigateButton1.setOnClickListener {
            val toDash = Intent(this, FriendDashBoard::class.java)
            startActivity(toDash)
        }


    }
}
