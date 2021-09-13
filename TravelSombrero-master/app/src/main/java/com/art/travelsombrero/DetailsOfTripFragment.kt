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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.net.URL
import java.util.ArrayList

class DetailsOfTripFragment(var dest: DestinationDataModel) : Fragment() {

    private lateinit var adapter: MeteoRecyclerViewAdapter
    val listData: ArrayList<MeteoDataModel> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    private var urlDays= "https://api.openweathermap.org/data/2.5/onecall?lat=40.7127281&lon=-74.0060152&units=metric&exclude=current,alerts,hourly,minutely&appid=3541a6b71392e3d36a797bb75bc11f50"
    private var urlCurrent = "https://api.openweathermap.org/data/2.5/onecall?lat=40.7127281&lon=-74.0060152&units=metric&exclude=dayly,alerts,hourly,minutely&appid=3541a6b71392e3d36a797bb75bc11f50"
    private var url_icon = "https://openweathermap.org/img/wn/"
    private var url_iconCurrent = "https://openweathermap.org/img/wn/"
    private var final_icon = "@2x.png"
    private var celsius = "°C"
    private var ms = " meter/sec"
    //temp_day, temp_min, temp_max, wind, icon, description
    var temp_day: ArrayList<String> = ArrayList()
    var temp_min: ArrayList<String> = ArrayList()
    var temp_max: ArrayList<String> = ArrayList()
    var wind: ArrayList<String> = ArrayList()
    var icon: ArrayList<String> = ArrayList()
    var description: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details_of_trip, container, false)

        var q = URL(urlCurrent).readText().split("current")
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
        var icon3 = icon2[0].substring(3,icon2[0].length-4).trim()
        var link_icon = url_iconCurrent+icon3+final_icon
        val temperaturaTextView = view.findViewById<TextView>(R.id.temperaturaTextView)
        temperaturaTextView.text = temp3.toString()
        Picasso.get().load(link_icon).into(view.findViewById<ImageView>(R.id.meteoIcon))

        initMeteo(view)

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

        return view
    }

    private fun initMeteo(view: View){
        recyclerView = view.findViewById(R.id.meteo_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)

        val copia = URL(urlDays).readText()
        var q1 = copia
        //temp_day
        var q2 = q1.split("\"day\":")
        var t = 0
        for (item in q2) {
            if (t % 2 !=0) {
                val q3 = item.split(".")
                q3[0].substring(1).trim()
                val q4 =q3[0] + celsius
                temp_day.add(q4)
            }
            t++
        }
        q1 = copia
        //temp_min
        q2 = q1.split("\"min\":")
        t = 0
        for (item in q2) {
            if (t != 0) {
                val q3 = item.split(".")
                q3[0].substring(1).trim()
                val q4 ="Min: " + q3[0] + celsius
                temp_min.add(q4)
            }
            t++
        }
        q1 = copia
        //temp_max
        q2 = q1.split("\"max\":")
        t = 0
        for (item in q2) {
            if (t != 0) {
                val q3 = item.split(".")
                q3[0].substring(1).trim()
                val q4 ="Max: " + q3[0] + celsius
                temp_max.add(q4)
            }
            t++
        }
        q1 = copia
        //wind
        q2 = q1.split("\"wind_speed\":")
        t = 0
        for (item in q2) {
            if (t != 0) {
                val q3 = item.split(",")
                q3[0].substring(1).trim()
                val q4 = "Wind Speed: " + q3[0] + ms
                wind.add(q4)
            }
            t++
        }
        q1 = copia
        //icon
        q2 = q1.split("\"icon\":")
        t = 0
        for (item in q2) {
            if (t != 0) {
                val q3 = item.substring(1,4).trim()
                val q4 = url_icon + q3 + final_icon
                icon.add(q4)
            }
            t++
        }
        q1 = copia
        //description
        q2 = q1.split("\"description\":")
        t = 0
        for (item in q2) {
            if (t != 0) {
                val q3 = item.split(",")
                description.add(q3[0].substring(1,q3[0].length-1).trim())
            }
            t++
        }

        var i=0
        while(i<description.size){
            if(temp_day.size>0 && temp_min.size>0 && temp_max.size>0 && wind.size>0 && icon.size>0 && description.size>0){
                var meteoItem = MeteoDataModel(temp_day[i],temp_min.get(i),temp_max.get(i),wind.get(i),icon.get(i),description.get(i))
                listData.add(meteoItem)
            }

            i++
        }


        recyclerView.adapter = MeteoRecyclerViewAdapter(listData)

    }

}