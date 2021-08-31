package com.art.travelsombrero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [MeteoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeteoFragment : Fragment() {

    private lateinit var adapter: MeteoRecyclerViewAdapter
    val listData: ArrayList<MeteoDataModel> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    private var url = "https://api.openweathermap.org/data/2.5/onecall?lat=40.7127281&lon=-74.0060152&units=metric&exclude=current,alerts,hourly,minutely&appid=3541a6b71392e3d36a797bb75bc11f50"
    private var url_icon = "https://openweathermap.org/img/wn/"
    private var final_icon = "@2x.png"
    private var celsius = " °C"
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meteo, container, false)
        initMeteo(view)
        return view
    }

    private fun initMeteo(view: View){
        recyclerView = view.findViewById(R.id.meteo_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val copia = URL(url).readText()
        var q1 = copia
        //temp_day
        var q2 = q1.split("\"day\":")
        var t = 0
        for (item in q2) {
            if (t % 2 !=0) {
                val q3 = item.split(".")
                q3[0].substring(1).trim()
                val q4 ="Current Temperature: " + q3[0] + celsius
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
                val q4 ="MIN: " + q3[0] + celsius
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
                val q4 ="MAX: " + q3[0] + celsius
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
            var meteoItem = MeteoDataModel(temp_day.get(i),temp_min.get(i),temp_max.get(i),wind.get(i),icon.get(i),description.get(i))
            listData.add(meteoItem)
            i++
        }

        recyclerView.adapter = MeteoRecyclerViewAdapter(listData, this)

    }

    class MeteoDataModel(val temp_day: String, val temp_min: String, val temp_max: String, val wind: String, val iconUrl: String,val description: String){
        constructor() : this("","","","", "", "")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() =
            MeteoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}