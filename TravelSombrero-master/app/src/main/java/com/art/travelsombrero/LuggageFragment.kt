package com.art.travelsombrero
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LuggageFragment(var tripname: String) : Fragment(), LuggageRecyclerViewAdapter.ClickListener {

    private lateinit var adapter: LuggageRecyclerViewAdapter
    val objectList: ArrayList<LuggageDataModel> = ArrayList()
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
        val view = inflater.inflate(R.layout.fragment_luggage, container, false)
        initRecyclerView(view)
        val viewbutton = view.findViewById<Button>(R.id.addButton)
        viewbutton.setOnClickListener{
            var intent = Intent(context, AddObjectActivity::class.java)
            intent.putExtra("tripname", tripname)
            startActivity(intent)
        }
        return view
    }


    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.luggage_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        fetchDataFirebase(recyclerView, this)
        recyclerView.adapter = LuggageRecyclerViewAdapter(objectList, this)

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

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

    private fun showDialog(viewHolder: RecyclerView.ViewHolder){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure want to delete item?")
        builder.setPositiveButton("Confirm") {dialog, which->
            val position = viewHolder.adapterPosition
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val name = objectList.get(position).object_name
            val refremove = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname/luggage/$name")
            refremove.removeValue()
            objectList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton("Cancel") {dialog, which->
            val position = viewHolder.adapterPosition
            adapter.notifyItemChanged(position)
        }
        builder.show()
    }

    private fun fetchDataFirebase(recyclerView: RecyclerView, context: LuggageFragment){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname/luggage")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(bool){
                    snapshot.children.forEach{
                        var luggage = it.getValue(LuggageDataModel::class.java)
                        if (luggage != null) {
                            objectList.add(luggage)
                        }
                    }
                }
                adapter = LuggageRecyclerViewAdapter(objectList, context)
                recyclerView.adapter = adapter
                bool = false
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onItemClick(luggageDataModel: LuggageDataModel) {

    }
}