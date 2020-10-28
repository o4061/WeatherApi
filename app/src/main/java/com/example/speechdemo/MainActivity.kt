package com.example.speechdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SPEECH_INPUT = 100
    private lateinit var retService: WeatherService
    private lateinit var weatherItem: WeatherResponse
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(WeatherService::class.java)

        voice_btn.setOnClickListener {
            speak();
        }
    }

    private fun speak() {
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        mIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something")

        try {
            startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK || null != data) {
                val res: ArrayList<String> =
                    data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                cityName = res[0]

                getWeather(cityName)
            }
        }
    }

    private fun getWeather(city: String) {
        val responseLiveData: LiveData<Response<WeatherResponse>> = liveData {
            val response = retService.getWeatherByCityName(city, "API_KEY", "metric")
            emit(response)

            if (response.isSuccessful && weatherItem.cod == 200) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)

                intent.putExtra("lon", weatherItem.coord.lon)
                intent.putExtra("lat", weatherItem.coord.lat)
                //intent.putExtra("weather_id", weatherItem.weather[0].id)
                intent.putExtra("main", weatherItem.weather[0].main)
                intent.putExtra("description", weatherItem.weather[0].description)
                intent.putExtra("icon", weatherItem.weather[0].icon)
                intent.putExtra("temp", weatherItem.main.temp)
                intent.putExtra("feels_like", weatherItem.main.feels_like)
                intent.putExtra("temp_min", weatherItem.main.temp_min)
                intent.putExtra("temp_max", weatherItem.main.temp_max)
                intent.putExtra("pressure", weatherItem.main.pressure)
                intent.putExtra("humidity", weatherItem.main.humidity)
                //intent.putExtra("visibility", weatherItem.visibility)
                intent.putExtra("speed", weatherItem.wind.speed)
                //intent.putExtra("deg", weatherItem.wind.deg)
                //intent.putExtra("all", weatherItem.clouds.all)
                //intent.putExtra("dt", weatherItem.dt)
                //intent.putExtra("type", weatherItem.sys.type)
                //intent.putExtra("sys_id", weatherItem.sys.id)
                intent.putExtra("country", weatherItem.sys.country)
                intent.putExtra("sunrise", weatherItem.sys.sunrise)
                intent.putExtra("sunset", weatherItem.sys.sunset)
                intent.putExtra("timezone", weatherItem.timezone)
                //intent.putExtra("id", weatherItem.id)
                intent.putExtra("name", weatherItem.name)
                //intent.putExtra("cod", weatherItem.cod)
                startActivity(intent)
            }
        }

        responseLiveData.observe(this, androidx.lifecycle.Observer {
            if (it.code() == 200) {
                weatherItem = it.body()!!
            } else {
                Toast.makeText(this@MainActivity, "City not found", Toast.LENGTH_SHORT).show()
            }
        })
    }
}