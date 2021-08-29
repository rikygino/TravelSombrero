package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import com.art.travelsombrero.databinding.ActivityModifyTripBinding
import com.squareup.picasso.Picasso
import java.net.URL

class ModifyTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyTripBinding
    private lateinit var dest : DestinationDataModel
    private lateinit var trip : TripDataModel
    private var url = "https://api.openweathermap.org/data/2.5/onecall?lat=40.7127281&lon=-74.0060152&units=metric&exclude=alerts,daily,hourly,minutely&appid=3541a6b71392e3d36a797bb75bc11f50"
    private var url_icon = "https://openweathermap.org/img/wn/"
    private var final_icon = "@2x.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModifyTripBinding.inflate(layoutInflater)
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
            trip = TripDataModel(extras.getSerializable("tripname").toString(),
                extras.getSerializable("depdate").toString(),
                extras.getSerializable("retdate").toString(),
                extras.getSerializable("city").toString())

            binding.cityName.text = dest.city
            binding.stateName.text = dest.state
            Picasso.get().load(dest.imageUrl).into(binding.cityImage)

            binding.modifyButton.setOnClickListener {
                var intent = Intent( this, LuggageActivity::class.java)
                val tripname = trip.tripname
                intent.putExtra("tripname", tripname)
                Log.d( "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ", "Email is $tripname")

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
            var celsius = "°C"
            temp3.append(temp2[0])
            temp3.append(celsius)
            var icon = q2.split("icon")
            var icon2 = icon[1].split(",")
            var icon3 = icon2[0].substring(3,icon2[0].length-5).trim()
            var link_icon = url_icon+icon3+final_icon
            binding.temperaturaTextView.text = temp3.toString()
            Picasso.get().load(link_icon).into(binding.meteoIcon)
        }
    }
}