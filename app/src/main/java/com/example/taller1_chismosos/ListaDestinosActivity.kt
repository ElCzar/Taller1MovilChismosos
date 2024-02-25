package com.example.taller1_chismosos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONObject

class ListaDestinosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_destinos)

        initList()
        listListener()
    }

    private fun initList() {
        val previousIntent = intent.extras

        val destinos = if (previousIntent?.getBoolean("favorites") == true) {
            ArrayList(MainActivity.favorites)
        } else {
            val filterJson = previousIntent?.getString("filter")
            loadJson(filterJson)
        }

        // Get the list of destinations
        val listViewDestinos = findViewById<ListView>(R.id.ListViewDestinos)

        // Use the adapter
        val adapterDestinos = ArrayAdapter(this, android.R.layout.simple_list_item_1, destinos)
        listViewDestinos.adapter = adapterDestinos
    }

    private fun listListener() {
        val listViewDestinos = findViewById<ListView>(R.id.ListViewDestinos)
        listViewDestinos.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, DetalleDestinoActivity::class.java)

            val bundle = Bundle()
            bundle.putString("destino", listViewDestinos.getItemAtPosition(position).toString())
            intent.putExtras(bundle)

            startActivity(intent)
        }
    }

    private fun loadJson(filterJson: String?) : ArrayList<String> {
        val jsonStr = applicationContext.assets.open("destinos.json").bufferedReader().use {
            it.readText()
        }

        return JsonLoad().loadJsonList(filterJson, jsonStr)
    }
}