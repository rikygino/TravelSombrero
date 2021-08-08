package com.art.travelsombrero

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(val listData: List<DataModel>, val clickListener: ClickListener): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.destination_recycler_row,parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var city: TextView
//        var imageUrl: ImageView
        var state: TextView
        init{
            city = view.findViewById(R.id.city_name)
//            imageUrl = view.findViewById(R.id.city_image)
            state = view.findViewById(R.id.state_name)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.city.text = listData.get(position).city
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))

        }
//        holder.imageUrl.text = listData.get(position).city
//        holder.itemView.setOnClickListener(){
//            clickListener.onItemClick(listData.get(position))
//
//        }
        holder.state.text = listData.get(position).state
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))

        }
    }

    interface ClickListener {
        fun onItemClick(dataModel: DataModel)
    }
}