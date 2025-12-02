package com.example.saacapp.ui.theme

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saacapp.adapter.PictogramAdapter
import com.example.saacapp.data.PictogramRepository
import com.example.saacapp.databinding.FragmentFrasesBinding
import com.example.saacapp.model.Pictogram
import java.util.Locale

class FrasesFragment : Fragment() {

    private var _binding: FragmentFrasesBinding? = null
    private val binding get() = _binding!!

    private lateinit var opcionesAdapter: PictogramAdapter
    private lateinit var fraseAdapter: PictogramAdapter

    private val opcionesActuales = mutableListOf<Pictogram>()
    private val fraseSeleccionada = mutableListOf<Pictogram>()
    private lateinit var todos: List<Pictogram>

    // TTS solo para decir la frase completa
    private lateinit var tts: TextToSpeech

    private enum class SimpleLayer { ROOT, SENTIR, QUIERO_ACCION, QUIERO_COMPLEMENTO }

    private val layerStack = mutableListOf(SimpleLayer.ROOT)
    private var currentLayer = SimpleLayer.ROOT
    private var currentAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale("es", "ES")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFrasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todos = PictogramRepository.loadPictograms(requireContext())

        // Opciones (capas)
        opcionesAdapter = PictogramAdapter(opcionesActuales) { pict ->
            onOpcionClick(pict)
        }

        // Frase formada
        fraseAdapter = PictogramAdapter(fraseSeleccionada)

        binding.recyclerOpciones.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerOpciones.adapter = opcionesAdapter

        binding.recyclerFrase.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerFrase.adapter = fraseAdapter

        mostrarCapa(SimpleLayer.ROOT)

        binding.btnBorrarUltimo.setOnClickListener { borrarUltimo() }
        binding.btnBorrarTodo.setOnClickListener { borrarTodo() }
        binding.btnDecirFrase.setOnClickListener { decirFrase() }
    }

    // ----- Lógica de capas -----

    private fun mostrarCapa(layer: SimpleLayer) {
        currentLayer = layer
        opcionesActuales.clear()

        when (layer) {
            SimpleLayer.ROOT -> {
                val raiz = todos.filter {
                    it.nombre.equals("sentir", ignoreCase = true) ||
                            it.nombre.equals("yo quiero", ignoreCase = true)
                }
                opcionesActuales.addAll(raiz)
            }

            SimpleLayer.SENTIR -> {
                opcionesActuales.addAll(
                    todos.filter {
                        (it.etiqueta.equals("emocion", ignoreCase = true) ||
                                it.etiqueta.equals("sentir", ignoreCase = true)) &&
                                !it.nombre.equals("sentir", ignoreCase = true)
                    }
                )
            }

            SimpleLayer.QUIERO_ACCION -> {
                opcionesActuales.addAll(
                    todos.filter {
                        it.etiqueta.equals("accion", ignoreCase = true) &&
                                !it.nombre.equals("yo quiero", ignoreCase = true)
                    }
                )
            }

            SimpleLayer.QUIERO_COMPLEMENTO -> {
                val action = currentAction?.lowercase()

                val etiquetas = when (action) {
                    "comer" -> listOf("comida")
                    "ir"    -> listOf("lugar")
                    "beber" -> listOf("liquido")
                    "jugar" -> listOf("juego", "juegos")
                    "buscar"-> listOf("persona", "personas")
                    else    -> listOf("objeto", "lugar")
                }

                opcionesActuales.addAll(
                    todos.filter { pict ->
                        etiquetas.any { et ->
                            pict.etiqueta.equals(et, ignoreCase = true)
                        }
                    }
                )
            }
        }

        opcionesAdapter.notifyDataSetChanged()
    }

    private fun onOpcionClick(p: Pictogram) {
        when (currentLayer) {
            SimpleLayer.ROOT -> {
                if (p.nombre.equals("sentir", ignoreCase = true)) {
                    // 👇 En vez de "sentir", generamos un pictograma lógico "me siento"
                    val sentirFrase = p.copy(nombre = "me siento")
                    fraseSeleccionada.add(sentirFrase)
                    fraseAdapter.notifyItemInserted(fraseSeleccionada.size - 1)

                    layerStack.add(SimpleLayer.SENTIR)
                    mostrarCapa(SimpleLayer.SENTIR)
                } else if (p.nombre.equals("yo quiero", ignoreCase = true)) {
                    // Modo "yo quiero" como antes
                    fraseSeleccionada.add(p)
                    fraseAdapter.notifyItemInserted(fraseSeleccionada.size - 1)

                    layerStack.add(SimpleLayer.QUIERO_ACCION)
                    mostrarCapa(SimpleLayer.QUIERO_ACCION)
                }
            }

            SimpleLayer.SENTIR -> {
                // Añadimos solo la emoción: "me siento" + emoción
                fraseSeleccionada.add(p)
                fraseAdapter.notifyItemInserted(fraseSeleccionada.size - 1)
                // No avanzamos a más capas en este modo
            }

            SimpleLayer.QUIERO_ACCION -> {
                // yo quiero + acción
                fraseSeleccionada.add(p)
                fraseAdapter.notifyItemInserted(fraseSeleccionada.size - 1)

                currentAction = p.nombre
                layerStack.add(SimpleLayer.QUIERO_COMPLEMENTO)
                mostrarCapa(SimpleLayer.QUIERO_COMPLEMENTO)
            }

            SimpleLayer.QUIERO_COMPLEMENTO -> {
                // complemento final
                fraseSeleccionada.add(p)
                fraseAdapter.notifyItemInserted(fraseSeleccionada.size - 1)
            }
        }
    }

    // ----- Botones laterales -----

    private fun borrarUltimo() {
        if (fraseSeleccionada.isEmpty()) return

        when (currentLayer) {
            SimpleLayer.QUIERO_COMPLEMENTO -> {
                // caso especial: quitar complemento y acción
                if (fraseSeleccionada.size >= 3) {
                    // quitar complemento
                    var idx = fraseSeleccionada.size - 1
                    fraseSeleccionada.removeAt(idx)
                    fraseAdapter.notifyItemRemoved(idx)

                    // quitar acción
                    idx = fraseSeleccionada.size - 1
                    fraseSeleccionada.removeAt(idx)
                    fraseAdapter.notifyItemRemoved(idx)
                } else {
                    // por si acaso, borramos solo uno
                    val idx = fraseSeleccionada.size - 1
                    fraseSeleccionada.removeAt(idx)
                    fraseAdapter.notifyItemRemoved(idx)
                }

                currentAction = null

                // volver a capa de acciones
                while (layerStack.isNotEmpty() && layerStack.last() == SimpleLayer.QUIERO_COMPLEMENTO) {
                    layerStack.removeLast()
                }
                val newLayer = layerStack.lastOrNull() ?: SimpleLayer.ROOT
                mostrarCapa(newLayer)
            }

            else -> {
                // comportamiento normal: borrar uno y subir capa si corresponde
                val lastIndex = fraseSeleccionada.size - 1
                fraseSeleccionada.removeAt(lastIndex)
                fraseAdapter.notifyItemRemoved(lastIndex)

                if (layerStack.size > 1) {
                    layerStack.removeLast()
                }
                val top = layerStack.last()
                mostrarCapa(top)

                // 👇 Si estamos en ROOT después de borrar, limpiar toda la frase
                if (top == SimpleLayer.ROOT) {
                    fraseSeleccionada.clear()
                    fraseAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun borrarTodo() {
        fraseSeleccionada.clear()
        fraseAdapter.notifyDataSetChanged()

        layerStack.clear()
        layerStack.add(SimpleLayer.ROOT)
        currentAction = null
        mostrarCapa(SimpleLayer.ROOT)
    }

    private fun decirFrase() {
        if (fraseSeleccionada.isEmpty()) return
        val texto = fraseSeleccionada.joinToString(" ") { it.nombre }
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "frase_simple")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}

