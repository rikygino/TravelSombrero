package com.art.travelsombrero

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.art.travelsombrero.databinding.FragmentDetailsOfTripBinding
import com.squareup.picasso.Picasso
import java.net.URL

class DetailsOfTripFragment(var dest: DestinationDataModel) : Fragment() {

    private var url = "https://api.openweathermap.org/data/2.5/onecall?lat=40.7127281&lon=-74.0060152&units=metric&exclude=alerts,daily,hourly,minutely&appid=3541a6b71392e3d36a797bb75bc11f50"
    private var url_icon = "https://openweathermap.org/img/wn/"
    private var final_icon = "@2x.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details_of_trip, container, false)
        val cityname = view.findViewById<TextView>(R.id.city_name)
        cityname.text = dest.city
        val statename = view.findViewById<TextView>(R.id.state_name)
        statename.text = dest.state
        Picasso.get().load(dest.imageUrl).into(view.findViewById<ImageView>(R.id.city_image))

        val nextButton = view.findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            var intent = Intent( context, InsertDataTripActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        val meteoButton = view.findViewById<Button>(R.id.meteoButton)
        meteoButton.setOnClickListener {
            var intent = Intent( context, MeteoActivity::class.java)
            startActivity(intent)
        }
        val NewsButton = view.findViewById<CardView>(R.id.NewsButton)
        NewsButton.setOnClickListener {
            var intent = Intent( context, NewsActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }
        val FoodButton = view.findViewById<CardView>(R.id.FoodButton)
        FoodButton.setOnClickListener {
            var intent = Intent( context, FoodActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }
        val TuristicButton = view.findViewById<CardView>(R.id.TuristicButton)
        TuristicButton.setOnClickListener {
            var intent = Intent( context, TuristicActivity::class.java)
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
        val temperaturaTextView = view.findViewById<TextView>(R.id.temperaturaTextView)
        temperaturaTextView.text = temp3.toString()
        Picasso.get().load(link_icon).into(view.findViewById<ImageView>(R.id.meteoIcon))

        return view
    }

}