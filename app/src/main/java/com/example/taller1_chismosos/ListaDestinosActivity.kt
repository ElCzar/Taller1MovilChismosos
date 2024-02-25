package com.example.taller1_chismosos

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
    }

    private fun initList() {
        val previousIntent = intent.extras
        val filterJson = previousIntent?.getString("filter")

        // Get the list of destinations
        val destinos = loadJson(filterJson)
        val listViewDestinos = findViewById<ListView>(R.id.ListViewDestinos)

        // Use the adapter
        val adapterDestinos = ArrayAdapter(this, android.R.layout.simple_list_item_1, destinos)
        listViewDestinos.adapter = adapterDestinos
    }

    private fun loadJson(filterJson: String?) : ArrayList<String> {
        val jsonStr = applicationContext.assets.open("destinos.json").bufferedReader().use {
            it.readText()
        }

        val jsonObj = JSONObject(jsonStr)
        val destinos = jsonObj.getJSONArray("destinos")

        val destinosList = ArrayList<String>()

        var filtro = ""
        if (filterJson != null && filterJson != "Todos") {
            filtro = filterJson
        }

        for (i in 0 until destinos.length()) {
            val destino = destinos.getJSONObject(i)
            // Get Json categories
            val nombre = destino.getString("nombre")
            val categoria = destino.getString("categoria")

            if(filtro in categoria) {
                destinosList.add(nombre)
            }
        }

        return destinosList
    }
}