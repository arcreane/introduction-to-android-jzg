package com.example.shaketorefreshjokes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnGoToJokes = findViewById<Button>(R.id.btnGoToJokes)
        btnGoToJokes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnAnotherFeature = findViewById<Button>(R.id.btnAnotherFeature)
        btnAnotherFeature.setOnClickListener {
        }
    }
}