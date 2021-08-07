package com.art.travelsombrero


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class NewTripFragment : Fragment(), RecyclerViewAdapter.ClickListener {

    private lateinit var adapter: RecyclerViewAdapter
    val listData: ArrayList<DataModel> = ArrayList()
    private var bool = false


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
        buildDisplayData()
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerViewAdapter(listData, this)
        recyclerView.adapter = adapter
    }

    private fun buildDisplayData(){
        listData.add(DataModel("BMW"))
        listData.add(DataModel("Audi"))
        listData.add(DataModel("Chevrolet"))
        listData.add(DataModel("Ford"))
        listData.add(DataModel("Honda"))
        listData.add(DataModel("Ferrari"))
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