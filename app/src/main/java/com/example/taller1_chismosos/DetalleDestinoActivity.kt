package com.example.taller1_chismosos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible

class DetalleDestinoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_destino)

        initDetalles()
        initButtons()
    }

    private fun initDetalles() {
        val previousIntent = intent.extras
        val destino = previousIntent?.getString("destino")

        // Set text views
        val nombre = findViewById<TextView>(R.id.textViewNombre)
        nombre.text = destino

        if (destino == "N/A") return

        // Get the details of the destination
        val detalles = loadJson(destino)

        val detalle = findViewById<TextView>(R.id.textViewDetalle)
        val detalleText = "${detalles["pais"]}\n${detalles["categoria"]}\n${detalles["plan"]}\nUSD ${detalles["precio"]}"
        detalle.text = detalleText
    }

    private fun initButtons() {
        val buttonFavoritos = findViewById<Button>(R.id.buttonFavoritos)

        if (intent.extras?.getBoolean("random") == true) {
            buttonFavoritos.isVisible = false
            return
        }

        // Check if the destination is already in favorites
        val nombre = findViewById<TextView>(R.id.textViewNombre).text.toString()
        buttonFavoritos.isEnabled = !MainActivity.favorites.contains(nombre)

        buttonFavoritos.setOnClickListener {
            // Add to favorites from MainActivity
            MainActivity.favorites.add(nombre)
            // Toast message and disable button
            Toast.makeText(applicationContext, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
            buttonFavoritos.isEnabled = false
        }
    }

    private fun loadJson(destino: String?) : MutableMap<String, String> {
        val jsonStr = applicationContext.assets.open("destinos.json").bufferedReader().use {
            it.readText()
        }

        var destinoJson = ""
        if (destino != null) {
            destinoJson = destino
        }

        return JsonLoad().loadJsonDetail(destinoJson, jsonStr)
    }
}