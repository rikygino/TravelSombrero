package com.art.travelsombrero

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NewTripFragment : Fragment(), RecyclerViewAdapter.ClickListener {

    private lateinit var adapter: RecyclerViewAdapter
    val listData: ArrayList<DataModel> = ArrayList()
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
        val view = inflater.inflate(R.layout.fragment_new_trip, container, false)
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        fetchDataFirebase(recyclerView, this)
    }


    private fun fetchDataFirebase(recyclerView: RecyclerView, context: NewTripFragment){
        val ref = FirebaseDatabase.getInstance().getReference("/destinations")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(bool){
                    snapshot.children.forEach{

                        val destination = it.getValue(DataModel::class.java)
                        if (destination != null) {
                            listData.add(destination)
                        }
                    }
                }
                adapter = RecyclerViewAdapter(listData, context)
                recyclerView.adapter = adapter
                bool = false
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NewTripFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(dataModel: DataModel) {
        var intent = Intent(context, DetailsOfTripActivity::class.java)
        startActivity(intent)
    }

}