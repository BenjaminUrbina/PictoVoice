package com.example.saacapp.model

data class Pictogram(
    val nombre: String,
    val etiqueta: String,
    val imagen: String? = null,        // para recursos de drawable (los actuales)
    val imagePath: String? = null,     // para imágenes personalizadas (ruta en filesDir)
    val isCustom: Boolean = false
)