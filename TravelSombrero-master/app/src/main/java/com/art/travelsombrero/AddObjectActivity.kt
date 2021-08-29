package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.art.travelsombrero.databinding.ActivityAddObjectBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddObjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddObjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_object)

        var tripname=""
        var extras = intent.extras
        if (extras != null) {
            tripname = extras.getSerializable("tripname").toString()
        }

        binding = ActivityAddObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AddButton.setOnClickListener {
            saveObjectOnDB(tripname)
        }
    }

    private fun saveObjectOnDB(tripname: String) {
        var objectname = binding.ObjectNameEditText.text.toString()
        var am = binding.AmountEditText.text.toString()
        if(objectname=="" || am==""){
            Toast.makeText(this, "Please enter object name and amount", Toast.LENGTH_LONG).show()
            return
        }
        else{
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/mytrips/$tripname/luggage/$objectname")
            var amount = Integer.parseInt(am)
            val ob = LuggageDataModel(objectname,amount)
            ref.setValue(ob)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
            var intent = Intent( this, LuggageActivity::class.java)
            intent.putExtra("tripname", tripname)
            startActivity(intent)
        }

    }
}

