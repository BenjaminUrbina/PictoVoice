package com.example.saacapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.saacapp.data.UserPrefs
import com.example.saacapp.databinding.ActivityUserTypeBinding

class UserTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNino.setOnClickListener {
            UserPrefs.setRole(this, isTutor = false)
            goToMain()
        }

        binding.btnTutor.setOnClickListener {
            UserPrefs.setRole(this, isTutor = true)
            goToMain()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))

    }
}
