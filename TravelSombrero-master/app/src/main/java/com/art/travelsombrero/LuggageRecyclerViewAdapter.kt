package com.art.travelsombrero

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager




class LuggageRecyclerViewAdapter(val objectList: List<LuggageDataModel>, val clickListener: ClickListener): RecyclerView.Adapter<LuggageRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.luggage_recycler_row,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return objectList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var objectname: TextView = view.findViewById(R.id.object_name)
        var amount: TextView = view.findViewById(R.id.amount)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.objectname.text = objectList.get(position).object_name
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(objectList.get(position))
        }

        holder.amount.text ="x"+objectList.get(position).amount.toString()
        holder.itemView.setOnClickListener() {
            clickListener.onItemClick(objectList.get(position))
        }
    }

    interface ClickListener {
        fun onItemClick(luggageDataModel: LuggageDataModel)
    }
}