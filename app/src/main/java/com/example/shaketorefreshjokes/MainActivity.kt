package com.example.shaketorefreshjokes

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector

    private var currentJoke: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvJoke = findViewById<TextView>(R.id.tvJoke)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        val btnFavorite = findViewById<Button>(R.id.btnFavorite)

        // Function to fetch jokes
        fun fetchJoke() {
            RetrofitInstance.api.getRandomJoke().enqueue(object : Callback<JokeResponse> {
                override fun onResponse(call: Call<JokeResponse>, response: Response<JokeResponse>) {
                    if (response.isSuccessful) {
                        val joke = response.body()
                        val jokeText = "${joke?.setup}\n\n${joke?.punchline}"
                        tvJoke.text = jokeText
                        currentJoke = jokeText
                    } else {
                        tvJoke.text = "Failed to load joke."
                        currentJoke = null
                    }
                }

                override fun onFailure(call: Call<JokeResponse>, t: Throwable) {
                    tvJoke.text = "Oops, something went wrong."
                    currentJoke = null
                }
            })
        }

        // Button click listener
        btnRefresh.setOnClickListener { fetchJoke() }

        btnFavorite.setOnClickListener {
            currentJoke?.let { jokeText ->
                addJokeToFavorites(jokeText)
                Toast.makeText(this, "Joke added to Favorites!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "No joke to favorite!", Toast.LENGTH_SHORT).show()
            }
        }

        // Shake detection setup
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector { fetchJoke() }

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    private fun addJokeToFavorites(joke: String) {
        val sharedPrefs = getSharedPreferences("my_jokes_prefs", MODE_PRIVATE)
        val existingFavorites = sharedPrefs.getStringSet("favorites", mutableSetOf()) ?: mutableSetOf()
        existingFavorites.add(joke)

        sharedPrefs.edit()
            .putStringSet("favorites", existingFavorites)
            .apply()
    }
}
