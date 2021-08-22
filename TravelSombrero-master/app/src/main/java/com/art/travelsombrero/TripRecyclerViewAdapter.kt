package com.art.travelsombrero

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class TripRecyclerViewAdapter(val listData: List<DestinationDataModel>, val clickListener: ClickListener): RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_recycler_row,parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var city: TextView = view.findViewById(R.id.city_name)
        var depDate: TextView = view.findViewById(R.id.)
        var retDate: TextView = view.findViewById(R.id.)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city.text = listData.get(position).city
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }

        holder.depDate.text = listData.get(position).
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }

        holder.depDate.text = listData.get(position).
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }
    }

    interface ClickListener {
        fun onItemClick(destinationDataModel: DestinationDataModel)
    }
}