package com.example.taller1_chismosos

import org.json.JSONObject

class JsonLoad {
    fun loadJsonList(filterJson: String?, jsonStr: String) : ArrayList<String> {
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
            val categoriaFiltro = destino.getString("categoria")

            if(filtro in categoriaFiltro) {
                destinosList.add(nombre)
            }
        }

        return destinosList
    }

    fun loadJsonDetail(destino: String, jsonStr: String) : MutableMap<String, String> {
        val jsonObj = JSONObject(jsonStr)
        val destinos = jsonObj.getJSONArray("destinos")

        val destinoDetail: MutableMap<String, String> = mutableMapOf()

        for (i in 0 until destinos.length()) {
            val destinoJson = destinos.getJSONObject(i)
            val nombre = destinoJson.getString("nombre")

            if(destino == nombre) {
                destinoDetail["nombre"] = nombre
                destinoDetail["pais"] = destinoJson.getString("pais")
                destinoDetail["categoria"] = destinoJson.getString("categoria")
                destinoDetail["plan"] = destinoJson.getString("plan")
                destinoDetail["precio"] = destinoJson.getString("precio")
            }
        }

        return destinoDetail
    }
}