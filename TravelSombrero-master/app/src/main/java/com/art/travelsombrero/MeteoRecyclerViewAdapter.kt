package com.art.travelsombrero

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class MeteoRecyclerViewAdapter(val objectList: List<DetailsOfTripFragment.MeteoDataModel>): RecyclerView.Adapter<MeteoRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview_meteo,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return objectList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var temp_day: TextView = view.findViewById(R.id.temp_day)
        var temp_min: TextView = view.findViewById(R.id.temp_min)
        var temp_max: TextView = view.findViewById(R.id.temp_max)
        var image: ImageView = view.findViewById(R.id.iconImageView)
        var wind : TextView = view.findViewById(R.id.wind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.temp_day.text = objectList.get(position).temp_day
        holder.temp_min.text = objectList.get(position).temp_min
        holder.temp_max.text = objectList.get(position).temp_max
        holder.wind.text = objectList.get(position).wind

        Picasso.get().load(objectList.get(position).iconUrl).into(holder.image)

    }

    interface ClickListener {
        fun onItemClick(meteoDataModel: DetailsOfTripFragment.MeteoDataModel)
    }
}