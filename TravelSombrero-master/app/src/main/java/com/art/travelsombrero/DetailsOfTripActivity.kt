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

        binding.NewsButton.setOnClickListener {
            var intent = Intent( this, NewsActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        binding.FoodButton.setOnClickListener {
            var intent = Intent( this, FoodActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        binding.TuristicButton.setOnClickListener {
            var intent = Intent( this, TuristicActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

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
