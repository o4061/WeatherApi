package com.example.speechdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_second.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class SecondActivity : AppCompatActivity() {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val formatted: String

        val extras = intent.extras
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            formatted = current.format(formatter)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val lon = extras?.get("lon") as Double
        val lat = extras?.get("lat") as Double
        val name = extras?.get("name") as String
        val country = extras?.get("country") as String
        val image = extras?.get("icon") as String
        val temp = extras?.get("temp")
        val feels = extras?.get("feels_like")
        val description = extras?.get("description") as String
        val speed = extras?.get("speed")
        val pressure = extras?.get("pressure") as Int
        var sunrise = extras?.get("sunrise")
        var sunset = extras?.get("sunset")
        val humidity = extras?.get("humidity") as Int

        sunset = SimpleDateFormat("h:ma").format(sunset)
        sunrise =  SimpleDateFormat("h:ma").format(sunrise)

        time_Date.text = formatted
        city_Country.text = "$name, $country"
        changeImage(image)
        tempView.text = temp.toString() + 0x00B0.toChar() + "C"
        feelsLike_description.text = "Feels like " + feels.toString() + 0x00B0.toChar() + "C. " +  description.capitalize()
        windSpeed.text = "$speed m/s"
        pressureView.text = "$pressure hPa"
        sunriseView.text = sunrise.toString()
        sunsetView.text = sunset.toString()
        humidityView.text = "$humidity %"

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 8f))
        })
    }

    private fun changeImage(img: String) {
        when (img) {
            "01d" -> weatherImage.setImageResource(R.drawable.ic_01d)
            "01n" -> weatherImage.setImageResource(R.drawable.ic_01n)
            "02d" -> weatherImage.setImageResource(R.drawable.ic_02d)
            "02n" -> weatherImage.setImageResource(R.drawable.ic_02n)
            "03d" -> weatherImage.setImageResource(R.drawable.ic_03d)
            "03n" -> weatherImage.setImageResource(R.drawable.ic_03n)
            "04d" -> weatherImage.setImageResource(R.drawable.ic_04d)
            "04n" -> weatherImage.setImageResource(R.drawable.ic_04n)
            "09d" -> weatherImage.setImageResource(R.drawable.ic_09d)
            "09n" -> weatherImage.setImageResource(R.drawable.ic_09n)
            "10d" -> weatherImage.setImageResource(R.drawable.ic_10d)
            "10n" -> weatherImage.setImageResource(R.drawable.ic_10n)
            "11d" -> weatherImage.setImageResource(R.drawable.ic_11d)
            "11n" -> weatherImage.setImageResource(R.drawable.ic_11n)
            "13d" -> weatherImage.setImageResource(R.drawable.ic_13d)
            "13n" -> weatherImage.setImageResource(R.drawable.ic_13n)
            "50d" -> weatherImage.setImageResource(R.drawable.ic_50d)
            "50n" -> weatherImage.setImageResource(R.drawable.ic_50n)
        }
    }
}