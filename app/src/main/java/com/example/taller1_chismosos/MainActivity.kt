package com.example.taller1_chismosos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtons()
    }

    private fun initButtons() {
        val buttonExplorarDestinos = findViewById<Button>(R.id.buttonExplorarDestinos)
        buttonExplorarDestinos.setOnClickListener {
            val intent = Intent(this, ListaDestinosActivity::class.java)
            // Gets the filter value
            val filterJson = findViewById<Spinner>(R.id.spinnerFiltros)

            intent.putExtra("filter", filterJson.selectedItem.toString())

            startActivity(intent)
        }

        val buttonFavoritos = findViewById<Button>(R.id.buttonFavoritos)
        buttonFavoritos.setOnClickListener {

        }

        val buttonRecomendaciones = findViewById<Button>(R.id.buttonRecomendaciones)
        buttonRecomendaciones.setOnClickListener {

        }
    }
}