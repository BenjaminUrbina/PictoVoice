package com.example.saacapp.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.saacapp.adapter.PictogramAdapter
import com.example.saacapp.data.PictogramRepository
import com.example.saacapp.databinding.FragmentBuscarBinding
import com.example.saacapp.model.Pictogram

class BuscarFragment : Fragment() {

    private var _binding: FragmentBuscarBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PictogramAdapter
    private val listaFiltrada = mutableListOf<Pictogram>()
    private lateinit var todos: List<Pictogram>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todos = PictogramRepository.loadPictograms(requireContext())
        adapter = PictogramAdapter(listaFiltrada)
        binding.recyclerBuscar.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerBuscar.adapter = adapter

        // Buscar pictograma por nombre
        binding.btnBuscar.setOnClickListener {
            val texto = binding.editBuscar.text.toString().trim().lowercase()
            if (texto.isNotEmpty()) {
                listaFiltrada.clear()
                listaFiltrada.addAll(todos.filter { it.nombre.lowercase().contains(texto) })
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Escribe una palabra para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        // Categorías
        binding.btnCategoriaCocina.setOnClickListener {
            mostrarCategoria("cocina")
        }
        binding.btnCategoriaAnimales.setOnClickListener {
            mostrarCategoria("animales")
        }
    }

    private fun mostrarCategoria(nombre: String) {
        listaFiltrada.clear()
        listaFiltrada.addAll(todos.filter { it.etiqueta.lowercase() == nombre })
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
