package com.art.travelsombrero

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TripFragment : Fragment(), TripRecyclerViewAdapter.ClickListener {

    private lateinit var adapter: TripRecyclerViewAdapter
    val listData: ArrayList<TripDataModel> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private var oldnew = true


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
        var view = inflater.inflate(R.layout.fragment_trip, container, false)
        initRecyclerView(view,oldnew)
        val oldbtn = view.findViewById<Button>(R.id.old_trips_button)
        val upcomingbtn = view.findViewById<Button>(R.id.upcoming_trips_button)
        oldbtn.setOnClickListener {
            if(oldnew){
                upcomingbtn.setBackgroundResource(R.drawable.squared_trip_navbar)
                oldbtn.setBackgroundResource(R.drawable.squared_trip_navbar_selected)
                oldnew = false
                initRecyclerView(view,oldnew)
            }
        }
        upcomingbtn.setOnClickListener {
            if(!oldnew){
                oldbtn.setBackgroundResource(R.drawable.squared_trip_navbar)
                upcomingbtn.setBackgroundResource(R.drawable.squared_trip_navbar_selected)
                oldnew = true
                initRecyclerView(view,oldnew)
            }
        }
        return view
    }

    private fun initRecyclerView(view: View, oldnew : Boolean) {
        recyclerView = view.findViewById(R.id.trips_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        listData.clear()
        fetchDataFirebase(recyclerView, this,view, oldnew)

        recyclerView.adapter = TripRecyclerViewAdapter(listData, this)

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDialog(viewHolder,view)
            }
        }

        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recyclerView)
    }

    private fun showDialog(viewHolder: RecyclerView.ViewHolder, view: View){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure want to delete item?")
        builder.setPositiveButton("Confirm") {dialog, which->
            val position = viewHolder.adapterPosition
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val tripname = listData.get(position).tripname
            val refremove = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname")
            refremove.removeValue()
            listData.removeAt(position)
            if(listData.isEmpty()){
                val noTrips = view.findViewById<TextView>(R.id.no_trips)
                noTrips.text = "You don't have planned any trip yet..."
            }
            adapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton("Cancel") {dialog, which->
            val position = viewHolder.adapterPosition
            adapter.notifyItemChanged(position)
        }
        builder.setOnCancelListener{
            val position = viewHolder.adapterPosition
            adapter.notifyItemChanged(position)

        }
        builder.show()
    }



    private fun fetchDataFirebase(recyclerView: RecyclerView, context: TripFragment, view: View, oldnew: Boolean){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val trip= it.getValue(TripDataModel::class.java)
                        val tripdata = trip?.retdate
                        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        var retdate = LocalDate.parse(tripdata,formatter)
                        var currentdate = LocalDate.now()
                        if (trip != null) {
                            if(oldnew&&((retdate.year>currentdate.year)||(retdate.year==currentdate.year&&retdate.monthValue>currentdate.monthValue)||(retdate.year==currentdate.year&&retdate.monthValue==currentdate.monthValue&&retdate.dayOfMonth>=currentdate.dayOfMonth))){
                                listData.add(trip)
                            }
                            else if((!oldnew)&&((retdate.year<currentdate.year)||(retdate.year==currentdate.year&&retdate.monthValue<currentdate.monthValue)||(retdate.year==currentdate.year&&retdate.monthValue==currentdate.monthValue&&retdate.dayOfMonth<currentdate.dayOfMonth))){
                                listData.add(trip)
                            }
                        }
                    }
                if(listData.isEmpty()&&oldnew){
                    val noTrips = view.findViewById<TextView>(R.id.no_trips)
                    noTrips.text = "You don't have planned any trip yet..."
                }
                else if(listData.isEmpty()&&!oldnew){
                    val noTrips = view.findViewById<TextView>(R.id.no_trips)
                    noTrips.text = "You don't have completed any trip yet..."
                }
                else{
                    val noTrips = view.findViewById<TextView>(R.id.no_trips)
                    noTrips.text = ""
                }
                adapter = TripRecyclerViewAdapter(listData, context)
                recyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(tripDatamodel: TripDataModel) {
        val ref = FirebaseDatabase.getInstance().getReference("/destinations/${tripDatamodel.city}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val destination= snapshot.getValue(DestinationDataModel::class.java)
                if(destination != null){
                    var intent = Intent(context, ModifyTripActivity::class.java)
                    val alpha_3 = destination.alpha_3
                    val imageUrl = destination.imageUrl
                    val locCode = destination.locCode
                    val state = destination.state
                    intent.putExtra("alpha_3", alpha_3)
                    intent.putExtra("imageUrl", imageUrl)
                    intent.putExtra("locCode", locCode)
                    intent.putExtra("state", state)
                    val tripname = tripDatamodel.tripname
                    val city = tripDatamodel.city
                    val depdate = tripDatamodel.depdate
                    val retdate = tripDatamodel.retdate
                    intent.putExtra("tripname", tripname)
                    intent.putExtra("city", city)
                    intent.putExtra("depdate", depdate)
                    intent.putExtra("retdate", retdate)
                    startActivity(intent)
                }else{
                    Toast.makeText(context,"Error!",Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}