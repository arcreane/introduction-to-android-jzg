package com.example.shaketorefreshjokes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var listViewFavorites: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        listViewFavorites = findViewById(R.id.lvFavorites)

        val sharedPrefs = getSharedPreferences("my_jokes_prefs", MODE_PRIVATE)
        val favoritesSet = sharedPrefs.getStringSet("favorites", setOf())
        val favoritesList = favoritesSet?.toList() ?: listOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoritesList)
        listViewFavorites.adapter = adapter
    }
}