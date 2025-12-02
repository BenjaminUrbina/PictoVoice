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
        binding.recyclerBuscar.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerBuscar.adapter = adapter

        // Buscar pictograma por nombre
        binding.btnBuscar.setOnClickListener {
            val texto = binding.editBuscar.text.toString().trim().lowercase()
            if (texto.isNotEmpty()) {
                listaFiltrada.clear()
                listaFiltrada.addAll(
                    todos.filter { it.nombre.lowercase().contains(texto) }
                )
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Escribe una palabra para buscar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // Categorías con imagen
        binding.catanimales.setOnClickListener {
            mostrarCategoria("animales")
        }

        binding.cataccion.setOnClickListener {
            mostrarCategoria("accion")
        }
        binding.catcomida.setOnClickListener {
            mostrarCategoria("comida", "liquido")
        }
        binding.catlugar.setOnClickListener {
            mostrarCategoria("lugar")
        }

        binding.catjuego.setOnClickListener {
            mostrarCategoria("juego")
        }
        binding.catpersona.setOnClickListener {
            mostrarCategoria("persona")
        }
        binding.catfavoritos.setOnClickListener {
            mostrarCategoria("favoritos")
        }
    }

    private fun mostrarCategoria(vararg nombres: String) {
        val nombresNormalizados = nombres.map { it.lowercase() }.toSet()

        listaFiltrada.clear()
        listaFiltrada.addAll(
            todos.filter { pictograma ->
                pictograma.etiqueta.lowercase() in nombresNormalizados
            }
        )
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

