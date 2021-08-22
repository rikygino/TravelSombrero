package com.art.travelsombrero

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripRecyclerViewAdapter(val listData: List<TripDataModel>, val clickListener: ClickListener): RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_recycler_row,parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tripName: TextView = view.findViewById(R.id.trip_name)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tripName.text = listData[position].tripName
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData[position])
        }
    }

    interface ClickListener {
        fun onItemClick(tripDataModel: TripDataModel)
    }
}