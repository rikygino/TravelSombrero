package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.art.travelsombrero.databinding.ActivityInsertDataTripBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InsertDataTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertDataTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertDataTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCreateLuggage.setOnClickListener {
            var tripname = binding.TripNameEditText.text.toString()
            var depdateday = binding.DepartureDateDatePicker.dayOfMonth
            var depdatemonth = binding.DepartureDateDatePicker.month+1
            depdatemonth.toString()
            var depdateyear = binding.DepartureDateDatePicker.year
            var depdate = depdateday.toString()+"/"+depdatemonth.toString()+"/"+depdateyear.toString()
            var retdateday = binding.ReturnDateDatePicker.dayOfMonth
            var retdatemonth = binding.ReturnDateDatePicker.month+1
            retdatemonth.toString()
            var retdateyear = binding.ReturnDateDatePicker.year
            var retdate = retdateday.toString()+"/"+retdatemonth.toString()+"/"+retdateyear.toString()
            Log.d( "AAAAAAAAAAAAAAAA",  "$depdate 00000000000 $retdate")
            if(tripname=="") {
                Toast.makeText(this, "Name your trip", Toast.LENGTH_LONG).show()
            }
            else if (depdateyear>retdateyear||(depdateyear==retdateyear&&depdatemonth>retdatemonth)||(depdateyear==retdateyear&&depdatemonth==retdatemonth&&depdateday>retdateday)){
                Toast.makeText(this, "Wow, you are a time traveller :)", Toast.LENGTH_LONG).show()
            }
            else{
                createLuggage(tripname,depdate,retdate)
            }
        }
    }

    private fun createLuggage(tripname: String, depdate: String, retdate: String) {
        var city=""
        var extras = intent.extras
        if (extras != null) {
            city = extras.getSerializable("city").toString()
        }

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val refchecktripname = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips")
        refchecktripname.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val tripdata = it.getValue(TripData::class.java)
                    if (tripdata != null) {
                        if (tripdata.tripname == tripname) {
                            Toast.makeText(this@InsertDataTripActivity, "There is already a trip with this name", Toast.LENGTH_LONG).show()
                            return
                        }
                    }
                }
                val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname")
                val td = TripData(tripname,depdate,retdate,city)

                ref.setValue(td)
                    .addOnSuccessListener {
                        var intent = Intent( this@InsertDataTripActivity, DefaultLuggageActivity::class.java)
                        intent.putExtra("tripname", tripname)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@InsertDataTripActivity, "Impossible to create the trip: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                val refread = FirebaseDatabase.getInstance().getReference("/default luggage")
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
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
}

class TripData(val tripname:String, val depdate: String, val retdate: String, val city: String){
    constructor() : this("","", "", "")
}


