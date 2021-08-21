package com.art.travelsombrero

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

        var extras = intent.extras
        if (extras != null) {
            dest = DestinationDataModel(
                extras.getSerializable("alpha_3").toString(),
                extras.getSerializable("city").toString(),
                extras.getSerializable("imageUrl").toString(),
                extras.getSerializable("locCode").toString(),
                extras.getSerializable("state").toString())
        }


        // TODO: add info covid, weather etc

    }

}