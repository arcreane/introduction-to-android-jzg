package com.example.shaketorefreshjokes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FavoritesActivity : AppCompatActivity() {

    private lateinit var listViewFavorites: ListView
    private lateinit var btnClearFavorites: Button
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var favoritesList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Initialize views
        listViewFavorites = findViewById(R.id.lvFavorites)
        btnClearFavorites = findViewById(R.id.btnClearFavorites)

        // Load favorites from SharedPreferences
        val sharedPrefs = getSharedPreferences("my_jokes_prefs", MODE_PRIVATE)
        val favoritesSet = sharedPrefs.getStringSet("favorites", setOf())
        favoritesList = favoritesSet?.toMutableList() ?: mutableListOf()

        // Set up the ListView adapter
        adapter = ArrayAdapter(this, R.layout.list_item, favoritesList)
        listViewFavorites.adapter = adapter

        // Update text color dynamically
        listViewFavorites.setOnItemClickListener { parent, view, position, id ->
            val textView = view as android.widget.TextView
            textView.setTextColor(ContextCompat.getColor(this, R.color.jokeTextColor))
        }

        // Set Clear All button click listener
        btnClearFavorites.setOnClickListener {
            clearFavorites(sharedPrefs)
        }
    }

    private fun clearFavorites(sharedPrefs: android.content.SharedPreferences) {
        // Clear SharedPreferences
        val editor = sharedPrefs.edit()
        editor.remove("favorites")
        editor.apply()

        // Clear the list and refresh the ListView
        favoritesList.clear()
        adapter.notifyDataSetChanged()
    }
}
