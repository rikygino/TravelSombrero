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
            if(tripname!="") {
                createLuggage()
            }
            else{
                Toast.makeText(this, "Name your trip", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun createLuggage() {
        var tripname = binding.TripNameEditText.text.toString()
        var depdateday = binding.DepartureDateDatePicker.dayOfMonth.toString()
        var depdatemonth = binding.DepartureDateDatePicker.month
        depdatemonth++
        depdatemonth.toString()
        var depdateyear = binding.DepartureDateDatePicker.year.toString()
        var depdate = depdateday+"/"+depdatemonth+"/"+depdateyear
        var retdateday = binding.ReturnDateDatePicker.dayOfMonth.toString()
        var retdatemonth = binding.ReturnDateDatePicker.month
        retdatemonth++
        retdatemonth.toString()
        var retdateyear = binding.ReturnDateDatePicker.year.toString()
        var retdate = retdateday+"/"+retdatemonth+"/"+retdateyear
        var city=""

        var extras = intent.extras
        if (extras != null) {
            city = extras.getSerializable("city").toString()
        }

        Log.d("Register Activity", "la città è $city")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname")
        val td = TripData(tripname,depdate,retdate,city)

        ref.setValue(td)
            .addOnSuccessListener {
                Log.d("Register Activity", "Utente salvato anche nel DB")
                var intent = Intent( this, LuggageActivity::class.java)
                intent.putExtra("tripname", tripname)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Impossibile salvare l'utente nel DB ${it.message}", Toast.LENGTH_LONG).show()
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
}

class TripData(val tripname:String, val depdate: String, val retdate: String, val city: String){
    constructor() : this("","", "", "")
}

