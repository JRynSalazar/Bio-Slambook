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
import com.example.myapplication.databinding.ActivityEditLoveRelationshipBinding
import com.example.myapplication.databinding.WellDoneBinding

class EditLoveRelationship : AppCompatActivity() {

    private lateinit var binding: ActivityEditLoveRelationshipBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLoveRelationshipBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences("lovePreference", MODE_PRIVATE)



        binding.back.setOnClickListener{
            val back = Intent(this, DashBoardViewer::class.java)
            startActivity(back)
            finish()
        }

        binding.save.setOnClickListener{
            saveData(sharedPreferences)
            showWellDonePopup{
                val back = Intent(this, MyHobbies::class.java)
                startActivity(back)
                finish()
            }
        }
        loadData(sharedPreferences)
    }
    private fun saveData(sharedPreferences: SharedPreferences) {

        val loveMean = binding.loveMean.text.toString()
        val relationship = binding.relationship.text.toString()
        val date = binding.date.text.toString()
        val partner = binding.partner.text.toString()
        val show = binding.show.text.toString()
        val soulmates = binding.soulmates.text.toString()
        val sweetest = binding.sweetest.text.toString()
        val conflicts = binding.conflicts.text.toString()
        val learned = binding.learned.text.toString()
        val advice = binding.advice.text.toString()


        val editor  = sharedPreferences.edit()
        editor.putString("loveMean", loveMean)
        editor.putString("relationship", relationship)
        editor.putString("date", date)
        editor.putString("partner", partner)
        editor.putString("show", show)
        editor.putString("soulmates", soulmates)
        editor.putString("sweetest", sweetest)
        editor.putString("conflicts", conflicts)
        editor.putString("learned", learned)
        editor.putString("advice", advice)

        editor.apply()
       // Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()

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
