package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditMyHobbiesBinding
import com.example.myapplication.databinding.WellDoneBinding

class EditMyHobbies : AppCompatActivity() {

    private lateinit var binding: ActivityEditMyHobbiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMyHobbiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("myHobbiesPreference", MODE_PRIVATE)



        binding.back.setOnClickListener{
            val back = Intent(this, MyHobbies::class.java)
            startActivity(back)
            finish()
        }

        binding.save.setOnClickListener{
            saveData(sharedPreferences)
            showWellDonePopup{
                val back = Intent(this, MyHobbies::class.java)
                startActivity(back)
                finish()}

        }
        loadData(sharedPreferences)


    }
    private fun saveData(sharedPreferences: SharedPreferences) {

        val favHobby = binding.favHobby.text.toString()
        val indoor = binding.indoor.text.toString()
        val skill = binding.skill.text.toString()
        val newHobby = binding.newHobby.text.toString()
        val reading = binding.reading.text.toString()
        val music = binding.music.text.toString()
        val cook = binding.cook.text.toString()
        val created = binding.created.text.toString()
        val movies = binding.movies.text.toString()
        val travel = binding.travel.text.toString()


        val editor  = sharedPreferences.edit()
        editor.putString("favHobby", favHobby)
        editor.putString("indoor", indoor)
        editor.putString("skill", skill)
        editor.putString("newHobby", newHobby)
        editor.putString("reading", reading)
        editor.putString("music", music)
        editor.putString("cook", cook)
        editor.putString("created", created)
        editor.putString("movies", movies)
        editor.putString("travel", travel)

        editor.apply()
       // Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()

    }

    private fun loadData(sharedPreferences: SharedPreferences) {
        binding.favHobby.setText(sharedPreferences.getString("favHobby", ""))
        binding.indoor.setText(sharedPreferences.getString("indoor", ""))
        binding.skill.setText(sharedPreferences.getString("skill", ""))
        binding.newHobby.setText(sharedPreferences.getString("newHobby", ""))
        binding.reading.setText(sharedPreferences.getString("reading", ""))
        binding.music.setText(sharedPreferences.getString("music", ""))
        binding.cook.setText(sharedPreferences.getString("cook", ""))
        binding.created.setText(sharedPreferences.getString("created", ""))
        binding.movies.setText(sharedPreferences.getString("movies", ""))
        binding.travel.setText(sharedPreferences.getString("travel", ""))

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