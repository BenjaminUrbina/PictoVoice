package com.example.saacapp.adapter

import android.graphics.BitmapFactory
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saacapp.R
import com.example.saacapp.databinding.ItemPictogramBinding
import com.example.saacapp.model.Pictogram
import java.util.Locale

class PictogramAdapter(
    private val items: List<Pictogram>,
    private val onClick: ((Pictogram) -> Unit)? = null
) : RecyclerView.Adapter<PictogramAdapter.ViewHolder>() {

    private var tts: TextToSpeech? = null

    class ViewHolder(val binding: ItemPictogramBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPictogramBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Inicializar TTS solo una vez
        if (tts == null) {
            tts = TextToSpeech(parent.context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale("es", "ES")
                }
            }
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictogram = items[position]
        // El texto lo vamos a ocultar en el XML, pero lo dejamos seteado
        holder.binding.txtNombre.text = pictogram.nombre

        val context = holder.itemView.context

        if (pictogram.isCustom && !pictogram.imagePath.isNullOrEmpty()) {
            val bitmap = BitmapFactory.decodeFile(pictogram.imagePath)
            holder.binding.imgPictogram.setImageBitmap(bitmap)
        } else {
            val resId = context.resources.getIdentifier(
                pictogram.imagen,
                "drawable",
                context.packageName
            )
            if (resId != 0) {
                holder.binding.imgPictogram.setImageResource(resId)
            } else {
                holder.binding.imgPictogram.setImageResource(R.drawable.ic_placeholder)
            }
        }

        // 👇 Click SOLO en la imagen:
        // - Llama al callback (para Frases)
        // - Reproduce el sonido (para toda la app)
        holder.binding.imgPictogram.setOnClickListener {
            onClick?.invoke(pictogram)
            tts?.speak(
                pictogram.nombre,
                TextToSpeech.QUEUE_FLUSH,
                null,
                pictogram.nombre
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
