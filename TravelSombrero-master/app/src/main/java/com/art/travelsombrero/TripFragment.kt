package com.art.travelsombrero

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TripFragment : Fragment(), TripRecyclerViewAdapter.ClickListener {

    val listData: ArrayList<TripDataModel> = ArrayList()
    private var bool = true

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
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.trips_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        fetchDataFirebase(recyclerView, this,view)
    }


    private fun fetchDataFirebase(recyclerView: RecyclerView, context: TripFragment,view: View){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(bool){
                    snapshot.children.forEach{
                        val trip = it.getValue(TripDataModel::class.java)
                        if (trip != null) {
                            listData.add(trip)
                        }
                    }
                }
                recyclerView.adapter = TripRecyclerViewAdapter(listData, context)
                bool = false
                if(listData.isEmpty()){
                    val noTrips = view.findViewById<TextView>(R.id.no_trips)
                    noTrips.text = "You don't have planned any trip yet..."
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onResume() {

        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            TripFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onItemClick(tripDatamodel: TripDataModel) {
        var intent = Intent(context, DetailsOfTripActivity::class.java)
        startActivity(intent)
    }


}