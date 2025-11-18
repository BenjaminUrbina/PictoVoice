package com.example.saacapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.saacapp.databinding.ActivityMainBinding
import com.example.saacapp.ui.theme.PictogramFragment
import com.example.saacapp.ui.theme.FrasesFragment
import com.example.saacapp.ui.theme.BuscarFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pantalla por defecto: Inicio
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PictogramFragment())
            .commit()

        binding.btnInicio.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PictogramFragment())
                .commit()
        }

        binding.btnFrases.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FrasesFragment())
                .commit()
        }

        binding.btnBuscar.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BuscarFragment())
                .commit()
        }
    }
}


