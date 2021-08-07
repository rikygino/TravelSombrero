package com.art.travelsombrero

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class NewTrip : Fragment(), RecyclerViewAdapter.ClickListener {

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
        if(!bool){
            buildDisplayData()
            bool=true
        }
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerViewAdapter(listData, this)
        recyclerView.adapter = adapter

        //right swipe on recycler row::
        val itemSwipe = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDialog(viewHolder)
            }
        }

        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recyclerView)
    }

    private fun showDialog(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete item.")
        builder.setPositiveButton("Confirm"){dialog, which ->
            val position = viewHolder.adapterPosition
            listData.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        builder.setNegativeButton("Cancel"){dialog, which ->
            val position = viewHolder.adapterPosition
            adapter.notifyItemChanged(position)
        }
        builder.show()
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
            NewTrip().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(dataModel: DataModel) {
        val fragment: Fragment = DetailFragment.newInstance(dataModel.title!!)
        val transaction  = activity?.supportFragmentManager!!.beginTransaction()
        transaction.hide(activity?.supportFragmentManager!!.findFragmentByTag("new_trip_fragment")!!)
        transaction.add(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}