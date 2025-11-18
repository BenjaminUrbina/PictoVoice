package com.example.saacapp.data

import android.content.Context
import com.example.saacapp.model.Pictogram
import org.json.JSONObject

object PictogramRepository {

    fun loadPictograms(context: Context): List<Pictogram> {
        val jsonString = context.assets.open("pictogramas.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("pictogramas")

        val list = mutableListOf<Pictogram>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                Pictogram(
                    nombre = obj.getString("nombre"),
                    etiqueta = obj.getString("etiqueta"),
                    imagen = obj.getString("imagen")
                )
            )
        }
        return list
    }
}
