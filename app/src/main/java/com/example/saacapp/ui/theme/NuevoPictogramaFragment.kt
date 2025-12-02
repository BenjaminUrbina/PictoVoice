package com.example.saacapp.ui.theme

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.saacapp.data.CustomPictogramStore
import com.example.saacapp.databinding.FragmentNuevoPictogramaBinding
import com.example.saacapp.model.Pictogram
import java.io.File
import java.io.FileOutputStream
import android.graphics.Matrix
import com.example.saacapp.data.PictogramCategories


class NuevoPictogramaFragment : Fragment() {

    private var _binding: FragmentNuevoPictogramaBinding? = null
    private val binding get() = _binding!!

    private var fotoBitmap: Bitmap? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {

                // 1️⃣ Normalizar orientación: si es más ancho que alto, lo rotamos 90°
                val corrected = if (bitmap.width > bitmap.height) {
                    val matrix = Matrix().apply { postRotate(90f) }
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                } else {
                    bitmap
                }

                // 2️⃣ Redimensionar a cuadrado (por ejemplo 512x512)
                val size = 512
                val resized = Bitmap.createScaledBitmap(corrected, size, size, true)

                fotoBitmap = resized
                binding.imgPreview.setImageBitmap(resized)
            } else {
                Toast.makeText(requireContext(), "No se tomó la foto", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNuevoPictogramaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 Spinner de categorías
        val adapterCategorias = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            PictogramCategories.ALL
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerCategoria.adapter = adapterCategorias

        binding.btnTomarFoto.setOnClickListener {
            takePictureLauncher.launch(null)
        }

        binding.btnGuardarPictograma.setOnClickListener {
            guardarPictograma()
        }
    }

    private fun guardarPictograma() {
        val nombre = binding.editNombre.text.toString().trim()
        val categoria = binding.spinnerCategoria.selectedItem.toString()
        val bitmap = fotoBitmap

        if (nombre.isEmpty() || categoria.isEmpty() || bitmap == null) {
            Toast.makeText(
                requireContext(),
                "Completa nombre, categoría y foto",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Guardar archivo en memoria interna
        val file = File(requireContext().filesDir, "pict_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        val pict = Pictogram(
            nombre = nombre,
            etiqueta = categoria,
            imagen = null,
            imagePath = file.absolutePath,
            isCustom = true
        )

        CustomPictogramStore.savePictogram(requireContext(), pict)

        Toast.makeText(requireContext(), "Pictograma guardado", Toast.LENGTH_SHORT).show()

        // 🔹 Limpiar formulario
        binding.editNombre.text?.clear()
        binding.spinnerCategoria.setSelection(0)
        binding.imgPreview.setImageBitmap(null)
        fotoBitmap = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
