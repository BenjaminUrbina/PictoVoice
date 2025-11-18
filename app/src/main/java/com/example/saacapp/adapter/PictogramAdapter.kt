package com.example.saacapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saacapp.databinding.ItemPictogramBinding
import com.example.saacapp.model.Pictogram
import com.example.saacapp.R

import android.speech.tts.TextToSpeech
import java.util.*

class PictogramAdapter(private val items: List<Pictogram>) :
    RecyclerView.Adapter<PictogramAdapter.ViewHolder>() {

    private var tts: TextToSpeech? = null

    class ViewHolder(val binding: ItemPictogramBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPictogramBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Inicializar TextToSpeech (solo una vez por adapter)
        if (tts == null) {
            tts = TextToSpeech(parent.context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale("es", "ES") // español de España
                }
            }
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictogram = items[position]
        val context = holder.itemView.context

        holder.binding.txtNombre.text = pictogram.nombre

        val resourceName = pictogram.imagen
            .substringBeforeLast(".")
            .lowercase()

        val resId = context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )

        if (resId != 0) {
            holder.binding.imgPictogram.setImageResource(resId)
        } else {
            holder.binding.imgPictogram.setImageResource(R.drawable.ic_placeholder)
        }

        // 🔊 Reproducir la palabra al tocar la imagen
        holder.binding.imgPictogram.setOnClickListener {
            tts?.speak(pictogram.nombre, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        tts?.stop()
    }
}
