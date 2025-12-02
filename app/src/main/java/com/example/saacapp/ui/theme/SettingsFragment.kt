package com.example.saacapp.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.saacapp.data.UserPrefs
import com.example.saacapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx = requireContext()

        // Cargar valores actuales
        binding.switchFrases.isChecked = UserPrefs.showFrases(ctx)
        binding.switchBuscar.isChecked = UserPrefs.showBuscar(ctx)
        binding.switchNuevo.isChecked = UserPrefs.showNuevo(ctx)

        binding.switchFrases.setOnCheckedChangeListener { _, isChecked ->
            UserPrefs.setShowFrases(ctx, isChecked)
        }

        binding.switchBuscar.setOnCheckedChangeListener { _, isChecked ->
            UserPrefs.setShowBuscar(ctx, isChecked)
        }

        binding.switchNuevo.setOnCheckedChangeListener { _, isChecked ->
            UserPrefs.setShowNuevo(ctx, isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

