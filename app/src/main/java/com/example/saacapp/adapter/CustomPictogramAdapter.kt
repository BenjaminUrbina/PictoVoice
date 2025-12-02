package com.example.saacapp.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saacapp.databinding.ItemCustomPictogramBinding
import com.example.saacapp.model.Pictogram

class CustomPictogramAdapter(
    private val items: MutableList<Pictogram>,
    private val onEdit: (Pictogram) -> Unit,
    private val onDelete: (Pictogram) -> Unit
) : RecyclerView.Adapter<CustomPictogramAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCustomPictogramBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCustomPictogramBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictogram = items[position]
        holder.binding.txtNombre.text = pictogram.nombre
        holder.binding.txtCategoria.text = "Categoría: ${pictogram.etiqueta}"

        if (!pictogram.imagePath.isNullOrEmpty()) {
            val bmp = BitmapFactory.decodeFile(pictogram.imagePath)
            holder.binding.imgPictogram.setImageBitmap(bmp)
        }

        holder.binding.btnEditar.setOnClickListener {
            onEdit(pictogram)
        }

        holder.binding.btnEliminar.setOnClickListener {
            onDelete(pictogram)
        }
    }

    override fun getItemCount() = items.size
}
