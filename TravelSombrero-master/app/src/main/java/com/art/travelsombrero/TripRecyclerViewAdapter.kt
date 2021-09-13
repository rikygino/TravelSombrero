package com.art.travelsombrero

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
        var depDate: TextView = view.findViewById(R.id.countdownTextView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tripName.text = listData.get(position).tripname
        holder.itemView.setOnClickListener(){
            clickListener.onItemClick(listData.get(position))
        }
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var depdate = LocalDate.parse(listData.get(position).depdate,formatter)
        var retdate = LocalDate.parse(listData.get(position).retdate,formatter)
        var currentdate = LocalDate.now()
        val diff = ChronoUnit.DAYS.between(currentdate,depdate)
        val diff2 = ChronoUnit.DAYS.between(retdate,currentdate)

        if(diff>0) {
            holder.depDate.text = diff.toString() + " days left"
            holder.itemView.setOnClickListener() {
                clickListener.onItemClick(listData.get(position))
            }
        }
        else if (diff2>0) {
            holder.depDate.text = "Return date: "+retdate
        }
        else {
            holder.depDate.text = "Trip in progress..."
        }
    }

    interface ClickListener {
        fun onItemClick(tripDataModel: TripDataModel)
    }
}