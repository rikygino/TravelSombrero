package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding
import com.squareup.picasso.Picasso
import java.net.URL

class DetailsOfTripActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsOfTripBinding
    private lateinit var dest : DestinationDataModel
    private var url = "https://api.openweathermap.org/data/2.5/onecall?lat=40.7127281&lon=-74.0060152&units=metric&exclude=alerts,daily,hourly,minutely&appid=3541a6b71392e3d36a797bb75bc11f50"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsOfTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StrictMode.enableDefaults()

        var extras = intent.extras
        if (extras != null) {
            dest = DestinationDataModel(
                extras.getSerializable("alpha_3").toString(),
                extras.getSerializable("city").toString(),
                extras.getSerializable("imageUrl").toString(),
                extras.getSerializable("locCode").toString(),
                extras.getSerializable("state").toString())
        }

        binding.cityName.text = dest.city
        binding.stateName.text = dest.state
        Picasso.get().load(dest.imageUrl).into(binding.cityImage)

        binding.nextButton.setOnClickListener {
            var intent = Intent( this, InsertDataTripActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        binding.NewsButton.setOnClickListener {
            var intent = Intent( this, NewsActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        binding.FoodButton.setOnClickListener {
            var intent = Intent( this, FoodActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        binding.TuristicButton.setOnClickListener {
            var intent = Intent( this, TuristicActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        var q = URL(url).readText().split("current")
        var q1 = q[1].split("temp")
        var q2 = q1[1]
        var temp = q1[1].split(",")
        var temp1 = temp[0].substring(2)
        var temp2 = temp1.split(".")
        var temp3 = StringBuilder()
        var r = " °C"
        temp3.append(temp2[0])
        temp3.append(r)
        var meteo = q2.split("description")
        var meteo2 = meteo[1].split(",")
        var meteo3 = meteo2[0].substring(3,meteo2[0].length-1)
        binding.meteoTextView.text = temp3.toString()
        binding.temperaturaTextView.text = meteo3
    }

}
