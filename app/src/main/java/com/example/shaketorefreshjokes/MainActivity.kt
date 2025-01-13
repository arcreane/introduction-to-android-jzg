package com.example.shaketorefreshjokes

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvJoke = findViewById<TextView>(R.id.tvJoke)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        // Function to fetch jokes
        fun fetchJoke() {
            RetrofitInstance.api.getRandomJoke().enqueue(object : Callback<JokeResponse> {
                override fun onResponse(call: Call<JokeResponse>, response: Response<JokeResponse>) {
                    if (response.isSuccessful) {
                        val joke = response.body()
                        tvJoke.text = "${joke?.setup}\n\n${joke?.punchline}"
                    } else {
                        tvJoke.text = "Failed to load joke."
                    }
                }

                override fun onFailure(call: Call<JokeResponse>, t: Throwable) {
                    tvJoke.text = "Error: ${t.message}"
                }
            })
        }

        // Button click listener
        btnRefresh.setOnClickListener { fetchJoke() }

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
}
