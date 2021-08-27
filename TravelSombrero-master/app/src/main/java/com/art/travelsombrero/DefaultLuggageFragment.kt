package com.art.travelsombrero

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.graphics.PixelFormat.TRANSPARENT
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DefaultLuggageFragment(var tripname: String) : Fragment(), DefaultLuggageRecyclerViewAdapter.ClickListener {

    private lateinit var adapter: DefaultLuggageRecyclerViewAdapter
    val activityList: ArrayList<DefaultLuggageDataModel> = ArrayList()
    private var bool = true
    val selectedList: ArrayList<DefaultLuggageDataModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_default_luggage, container, false)
        initRecyclerView(view)
        val nextbtn = view.findViewById<Button>(R.id.next_default_luggage_fragment)
        nextbtn.setOnClickListener {
            loadDefaultLuggage()
            var intent = Intent(context, LuggageActivity::class.java)
            intent.putExtra("tripname", tripname)
            startActivity(intent)
        }
        return view
    }

    private fun loadDefaultLuggage() {
        var i=0
        val uid = FirebaseAuth.getInstance().uid ?: ""
        while(i<selectedList.size){
            val refread = FirebaseDatabase.getInstance().getReference("/default luggages/${selectedList[i].activity}/luggage")
            refread.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val luggage = it.getValue(LuggageDataModel::class.java)
                        if (luggage != null) {
                            val refwrite = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname/luggage/${luggage.object_name}")
                            refwrite.setValue(luggage)
                                .addOnSuccessListener {
                                }
                                .addOnFailureListener {
                                }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
            i++
        }
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.default_luggage_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        fetchDataFirebase(recyclerView, this)
        recyclerView.adapter = DefaultLuggageRecyclerViewAdapter(activityList, selectedList, this)
    }

    private fun fetchDataFirebase(recyclerView: RecyclerView, context: DefaultLuggageFragment) {
        val ref = FirebaseDatabase.getInstance().getReference("/default luggages")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (bool) {
                    snapshot.children.forEach {
                        var activity = it.getValue(DefaultLuggageDataModel::class.java)
                        if (activity != null) {
                            activityList.add(activity)
                        }
                    }
                }
                adapter = DefaultLuggageRecyclerViewAdapter(activityList, selectedList, context)
                recyclerView.adapter = adapter
                bool = false
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(get: DefaultLuggageDataModel) {
    }
}

