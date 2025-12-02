package com.example.saacapp.ui.theme

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saacapp.adapter.CustomPictogramAdapter
import com.example.saacapp.data.CustomPictogramStore
import com.example.saacapp.data.PictogramCategories
import com.example.saacapp.databinding.DialogEditarPictogramaBinding
import com.example.saacapp.databinding.FragmentMisPictogramasBinding
import com.example.saacapp.model.Pictogram

class MisPictogramasFragment : Fragment() {

    private var _binding: FragmentMisPictogramasBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CustomPictogramAdapter
    private val customList = mutableListOf<Pictogram>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPictogramasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar solo pictogramas personalizados
        customList.clear()
        customList.addAll(CustomPictogramStore.loadPictograms(requireContext()))

        adapter = CustomPictogramAdapter(
            items = customList,
            onEdit = { editarPictograma(it) },
            onDelete = { eliminarPictograma(it) }
        )

        binding.recyclerCustom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCustom.adapter = adapter
    }

    private fun editarPictograma(p: Pictogram) {
        val dialogBinding = DialogEditarPictogramaBinding.inflate(layoutInflater)

        dialogBinding.editNombre.setText(p.nombre)

        val catAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            PictogramCategories.ALL
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        dialogBinding.spinnerCategoria.adapter = catAdapter

        val index = PictogramCategories.ALL.indexOfFirst {
            it.equals(p.etiqueta, ignoreCase = true)
        }
        if (index >= 0) dialogBinding.spinnerCategoria.setSelection(index)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar pictograma")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = dialogBinding.editNombre.text.toString().trim()
                val nuevaCategoria =
                    dialogBinding.spinnerCategoria.selectedItem.toString()

                val actualizado = p.copy(
                    nombre = nuevoNombre,
                    etiqueta = nuevaCategoria
                )

                // 🔧 actualizar en almacenamiento
                CustomPictogramStore.updateCustomPictogram(requireContext(), actualizado)

                // 🔄 actualizar en lista local
                val idx = customList.indexOfFirst { it.imagePath == p.imagePath }
                if (idx >= 0) {
                    customList[idx] = actualizado
                    adapter.notifyItemChanged(idx)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarPictograma(p: Pictogram) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar pictograma")
            .setMessage("¿Seguro que deseas eliminar \"${p.nombre}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                // 🗑 borrar de almacenamiento
                CustomPictogramStore.deleteCustomPictogram(requireContext(), p)

                // 🔄 borrar de la lista local
                val idx = customList.indexOfFirst { it.imagePath == p.imagePath }
                if (idx >= 0) {
                    customList.removeAt(idx)
                    adapter.notifyItemRemoved(idx)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

