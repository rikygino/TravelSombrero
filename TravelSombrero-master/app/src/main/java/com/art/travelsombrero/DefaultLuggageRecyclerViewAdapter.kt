package com.art.travelsombrero

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlin.coroutines.coroutineContext

class DefaultLuggageRecyclerViewAdapter(val activityList: List<DefaultLuggageDataModel>, val selectedList: ArrayList<DefaultLuggageDataModel>, val clickListener: DefaultLuggageFragment): RecyclerView.Adapter<DefaultLuggageRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.default_luggage_recycler_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var activityname: TextView = view.findViewById(R.id.activity_name)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var cardview = holder.itemView.findViewById<TextView>(R.id.activity_name)
        holder.activityname.text = activityList.get(position).activity
        holder.itemView.setOnClickListener() {
            clickListener.onItemClick(activityList[position])
            if(selectedList.contains(activityList[position])) {
                cardview.setBackgroundResource(R.drawable.rounded_activities)
                selectedList.remove(activityList[position])
            }
            else {
                cardview.setBackgroundResource(R.drawable.rounded_activities_selected)
                selectedList.add(activityList[position])
            }
        }
    }

    interface ClickListener {
        fun onItemClick(luggageDataModel: DefaultLuggageDataModel)
    }
}