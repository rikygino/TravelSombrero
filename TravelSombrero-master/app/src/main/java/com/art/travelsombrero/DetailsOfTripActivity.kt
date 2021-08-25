package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding

class DetailsOfTripActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsOfTripBinding
    private lateinit var dest : DestinationDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsOfTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextButton.setOnClickListener {
            var intent = Intent( this, InsertDataTripActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        //Button per Abi
//        binding.nextButton.setOnClickListener {
//            var intent = Intent( this, InsertDataTripActivity::class.java)
//            startActivity(intent)
//        }
//        binding.nextButton.setOnClickListener {
//            var intent = Intent( this, InsertDataTripActivity::class.java)
//            startActivity(intent)
//        }
//        binding.nextButton.setOnClickListener {
//            var intent = Intent( this, InsertDataTripActivity::class.java)
//            startActivity(intent)
//        }

        var extras = intent.extras
        if (extras != null) {
            dest = DestinationDataModel(
                extras.getSerializable("alpha_3").toString(),
                extras.getSerializable("city").toString(),
                extras.getSerializable("imageUrl").toString(),
                extras.getSerializable("locCode").toString(),
                extras.getSerializable("state").toString())
        }
    }

}
