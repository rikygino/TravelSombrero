package com.art.travelsombrero

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DestinationRecyclerViewAdapter(val listData: List<DestinationDataModel>, val clickListener: ClickListener): RecyclerView.Adapter<DestinationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.destination_recycler_row,parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var city: TextView = view.findViewById(R.id.city_name)
        var imageUrl: ImageView = view.findViewById(R.id.city_image)
        var state: TextView = view.findViewById(R.id.state_name)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city.text = listData.get(position).city
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }

        Picasso.get().load(listData.get(position).imageUrl).into(holder.imageUrl)
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }

        holder.state.text = listData.get(position).state
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }
    }

    interface ClickListener {
        fun onItemClick(destinationDataModel: DestinationDataModel)
    }
}