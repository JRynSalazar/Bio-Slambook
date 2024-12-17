package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditFunRandomBinding
import com.example.myapplication.databinding.WellDoneBinding

class EditFunRandom : AppCompatActivity() {

    private lateinit var binding: ActivityEditFunRandomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFunRandomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("funRandomPreference", MODE_PRIVATE)



        binding.save.setOnClickListener{
            saveData(sharedPreferences)
            showWellDonePopup{
                val back = Intent(this, MyHobbies::class.java)
                startActivity(back)
                finish()
            }
        }

        binding.back.setOnClickListener{
            val toAboutME = Intent(this, FunRandom::class.java)
            startActivity(toAboutME)
            finish()
        }
        loadData(sharedPreferences)
    }

    private fun saveData(sharedPreferences: SharedPreferences) {

        val pet = binding.pet.text.toString()
        val crazy = binding.crazy.text.toString()
        val superpower = binding.superpower.text.toString()
        val moment = binding.moment.text.toString()
        val stranded = binding.stranded.text.toString()
        val food = binding.food.text.toString()
        val time = binding.time.text.toString()
        val dreamWeird = binding.dreamWeird.text.toString()
        val lottery = binding.lottery.text.toString()
        val swapLives = binding.swapLives.text.toString()


        val editor  = sharedPreferences.edit()
        editor.putString("pet", pet)
        editor.putString("crazy", crazy)
        editor.putString("superpower", superpower)
        editor.putString("moment", moment)
        editor.putString("stranded", stranded)
        editor.putString("food", food)
        editor.putString("time", time)
        editor.putString("dreamWeird", dreamWeird)
        editor.putString("lottery", lottery)
        editor.putString("swapLives", swapLives)

        editor.apply()
       // Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()

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