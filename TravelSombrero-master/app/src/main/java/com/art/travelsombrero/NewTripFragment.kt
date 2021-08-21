package com.art.travelsombrero

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NewTripFragment : Fragment(), RecyclerViewAdapter.ClickListener {

    private lateinit var adapter: RecyclerViewAdapter
    val listData: ArrayList<DestinationDataModel> = ArrayList()
    val searchedListData: ArrayList<DestinationDataModel> = ArrayList()
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        fetchDataFirebase(recyclerView, this)
        val searchRecyclerView = view.findViewById<SearchView>(R.id.searchView)
        searchRecyclerView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchRecyclerView.clearFocus()
                var index=0
                searchedListData.clear()
                while(listData.size != index){

                    if(listData.get(index).city.length >= query.toString().length
                        && listData.get(index).city.substring(0,query.toString().length).lowercase() == query.toString().lowercase() ){
                        searchedListData.add(listData[index])
                    }
                    index++
                }
                recyclerView.adapter = RecyclerViewAdapter(searchedListData, this@NewTripFragment)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var index=0
                searchedListData.clear()
                while(listData.size != index){

                    if(listData.get(index).city.length >= newText.toString().length
                        && listData.get(index).city.substring(0,newText.toString().length).lowercase() == newText.toString().lowercase() ){
                        searchedListData.add(listData[index])
                    }
                    index++
                }
                recyclerView.adapter = RecyclerViewAdapter(searchedListData, this@NewTripFragment)
                return false
            }
        })
    }


    private fun fetchDataFirebase(recyclerView: RecyclerView, context: NewTripFragment){
        val ref = FirebaseDatabase.getInstance().getReference("/destinations")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(bool){
                    snapshot.children.forEach{

                        val destination = it.getValue(DestinationDataModel::class.java)
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

    override fun onItemClick(destinationDataModel: DestinationDataModel) {
        var intent = Intent(context, DetailsOfTripActivity::class.java)
        val alpha_3 = destinationDataModel.alpha_3
        val city = destinationDataModel.city
        val imageUrl = destinationDataModel.imageUrl
        val locCode = destinationDataModel.locCode
        val state = destinationDataModel.state
        intent.putExtra("alpha_3", alpha_3)
        intent.putExtra("city", city)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("locCode", locCode)
        intent.putExtra("state", state)
        startActivity(intent)
    }

}

