package com.example.saacapp.data

import android.content.Context
import com.example.saacapp.model.Pictogram
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object CustomPictogramStore {

    private const val PREFS_NAME = "custom_pictograms"
    private const val KEY_DATA = "data"

    // --- helpers internos ---

    private fun loadJsonArray(context: Context): JSONArray {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_DATA, "[]") ?: "[]"
        return JSONArray(raw)
    }

    private fun saveJsonArray(context: Context, array: JSONArray) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_DATA, array.toString()).apply()
    }

    private fun pictogramFromJson(obj: JSONObject): Pictogram {
        return Pictogram(
            nombre = obj.getString("nombre"),
            etiqueta = obj.getString("etiqueta"),
            imagen = obj.optString("imagen", null),
            imagePath = obj.optString("imagePath", null),
            isCustom = true
        )
    }

    private fun jsonFromPictogram(p: Pictogram): JSONObject =
        JSONObject().apply {
            put("nombre", p.nombre)
            put("etiqueta", p.etiqueta)
            put("imagen", p.imagen ?: JSONObject.NULL)
            put("imagePath", p.imagePath ?: JSONObject.NULL)
        }

    // --- API pública ---

    /** Obtener todos los pictogramas personalizados */
    fun loadPictograms(context: Context): List<Pictogram> {
        val arr = loadJsonArray(context)
        val list = mutableListOf<Pictogram>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            list.add(pictogramFromJson(obj))
        }
        return list
    }

    /** Guardar nuevo pictograma personalizado (append) */
    fun savePictogram(context: Context, pictogram: Pictogram) {
        val arr = loadJsonArray(context)
        arr.put(jsonFromPictogram(pictogram))
        saveJsonArray(context, arr)
    }

    /** 🔧 Actualizar nombre / categoría de un pictograma personalizado */
    fun updateCustomPictogram(context: Context, updated: Pictogram) {
        val arr = loadJsonArray(context)

        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val path = obj.optString("imagePath", null)
            // usamos imagePath como identificador estable
            if (path == updated.imagePath) {
                obj.put("nombre", updated.nombre)
                obj.put("etiqueta", updated.etiqueta)
                arr.put(i, obj)
                break
            }
        }

        saveJsonArray(context, arr)
    }

    /** 🗑 Eliminar pictograma personalizado (y opcionalmente su archivo de imagen) */
    fun deleteCustomPictogram(context: Context, target: Pictogram) {
        val arr = loadJsonArray(context)
        val newArr = JSONArray()

        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val path = obj.optString("imagePath", null)
            if (path != target.imagePath) {
                newArr.put(obj)
            }
        }

        saveJsonArray(context, newArr)

        // Opcional: borrar archivo de imagen del almacenamiento
        target.imagePath?.let { path ->
            val f = File(path)
            if (f.exists()) {
                f.delete()
            }
        }
    }
}

