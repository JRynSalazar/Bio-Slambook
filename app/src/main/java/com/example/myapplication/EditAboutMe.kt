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
import com.example.myapplication.databinding.ActivityEditAboutMeBinding
import com.example.myapplication.databinding.WellDoneBinding

class EditAboutMe : AppCompatActivity() {

    private lateinit var binding: ActivityEditAboutMeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditAboutMeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences: SharedPreferences = getSharedPreferences("aboutMEPreference", MODE_PRIVATE)

        loadData(sharedPreferences)

        binding.save.setOnClickListener {
            saveData(sharedPreferences)
            showWellDonePopup{
                val back = Intent(this, AboutMe::class.java)
                startActivity(back)
                finish()
            }
        }


        binding.back.setOnClickListener {
            val toAboutME = Intent(this, AboutMe::class.java)
            startActivity(toAboutME)
            finish()
        }
    }

    private fun saveData(sharedPreferences: SharedPreferences) {
        val nickname = binding.nickName.text.toString()
        val personality1 = binding.personality1.text.toString()
        val personality2 = binding.personality2.text.toString()
        val personality3 = binding.personality3.text.toString()
        val dream = binding.dream.text.toString()
        val motivates = binding.motivates.text.toString()
        val talent = binding.talent.text.toString()
        val motto = binding.motto.text.toString()
        val liveWithout1 = binding.liveWithout1.text.toString()
        val liveWithout2 = binding.liveWithout2.text.toString()
        val liveWithout3 = binding.liveWithout3.text.toString()

        val editor = sharedPreferences.edit()
        editor.putString("nickname", nickname)
        editor.putString("personality1", personality1)
        editor.putString("personality2", personality2)
        editor.putString("personality3", personality3)
        editor.putString("dream", dream)
        editor.putString("motivates", motivates)
        editor.putString("talent", talent)
        editor.putString("motto", motto)
        editor.putString("liveWithout1", liveWithout1)
        editor.putString("liveWithout2", liveWithout2)
        editor.putString("liveWithout3", liveWithout3)
        editor.apply()

       // Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadData(sharedPreferences: SharedPreferences) {
        binding.nickName.setText(sharedPreferences.getString("nickname", ""))
        binding.personality1.setText(sharedPreferences.getString("personality1", ""))
        binding.personality2.setText(sharedPreferences.getString("personality2", ""))
        binding.personality3.setText(sharedPreferences.getString("personality3", ""))
        binding.dream.setText(sharedPreferences.getString("dream", ""))
        binding.motivates.setText(sharedPreferences.getString("motivates", ""))
        binding.talent.setText(sharedPreferences.getString("talent", ""))
        binding.motto.setText(sharedPreferences.getString("motto", ""))
        binding.liveWithout1.setText(sharedPreferences.getString("liveWithout1", ""))
        binding.liveWithout2.setText(sharedPreferences.getString("liveWithout2", ""))
        binding.liveWithout3.setText(sharedPreferences.getString("liveWithout3", ""))
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
