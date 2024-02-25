package com.example.taller1_chismosos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        val favorites: MutableSet<String> = mutableSetOf()
    }

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
            val intent = Intent(this, ListaDestinosActivity::class.java)

            intent.putExtra("favorites", true)

            startActivity(intent)
        }

        val buttonRecomendaciones = findViewById<Button>(R.id.buttonRecomendaciones)
        buttonRecomendaciones.setOnClickListener {
            if(favorites.isEmpty()) {
                Toast.makeText(applicationContext, "No hay destinos favoritos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, DetalleDestinoActivity::class.java)

            val random = (0 until favorites.size).random()

            val bundle = Bundle()

            bundle.putBoolean("random", true)
            bundle.putString("destino", favorites.elementAt(random))

            intent.putExtras(bundle)

            startActivity(intent)
        }
    }
}