package com.example.saacapp

import com.example.saacapp.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.saacapp.data.UserPrefs
import com.example.saacapp.databinding.ActivityMainBinding
import com.example.saacapp.ui.theme.BuscarFragment
import com.example.saacapp.ui.theme.FrasesFragment
import com.example.saacapp.ui.theme.NuevoPictogramaFragment
import com.example.saacapp.ui.theme.PictogramFragment
import com.example.saacapp.ui.theme.SettingsFragment
import com.example.saacapp.ui.theme.MisPictogramasFragment
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isTutor = UserPrefs.isTutor(this)

        // Configurar visibilidad de botones
        if (isTutor) {
            // El tutor ve todo
            binding.btnFrases.isVisible = true
            binding.btnBuscar.isVisible = true
            binding.btnOpciones.isVisible = true
            binding.btnNuevo.isVisible = true
        } else {
            // Niño: solo lo que el tutor permita
            binding.btnFrases.isVisible = UserPrefs.showFrases(this)
            binding.btnBuscar.isVisible = UserPrefs.showBuscar(this)
            binding.btnOpciones.isVisible = false
            binding.btnNuevo.isVisible = UserPrefs.showNuevo(this)
        }


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

        binding.btnOpciones.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .commit()
        }

        binding.btnMisPictos.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MisPictogramasFragment())
                .commit()
        }

        binding.btnNuevo.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NuevoPictogramaFragment())
                .commit()
        }

    }
}


