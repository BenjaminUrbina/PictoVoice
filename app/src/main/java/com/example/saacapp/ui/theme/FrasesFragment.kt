package com.example.saacapp.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.saacapp.adapter.PictogramAdapter
import com.example.saacapp.data.PictogramRepository
import com.example.saacapp.databinding.FragmentFrasesBinding

class FrasesFragment : Fragment() {

    private var _binding: FragmentFrasesBinding? = null
    private val binding get() = _binding!!

    private val seleccionados = mutableListOf<com.example.saacapp.model.Pictogram>()
    private lateinit var adapter: PictogramAdapter
    private val todos = mutableListOf<com.example.saacapp.model.Pictogram>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFrasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todos.addAll(PictogramRepository.loadPictograms(requireContext()))
        adapter = PictogramAdapter(seleccionados)
        binding.recyclerFrases.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerFrases.adapter = adapter

        // Buscar pictogramas
        binding.btnBuscar.setOnClickListener {
            val query = binding.editBuscar.text.toString().trim().lowercase()
            val encontrado = todos.find { it.imagen.substringBeforeLast(".").lowercase() == query }
            if (encontrado != null) {
                seleccionados.add(encontrado)
                adapter.notifyItemInserted(seleccionados.size - 1)
            }
        }

        // Limpiar
        binding.btnLimpiar.setOnClickListener {
            seleccionados.clear()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
