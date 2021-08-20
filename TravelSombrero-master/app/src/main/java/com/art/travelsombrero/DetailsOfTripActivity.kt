package com.art.travelsombrero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding

class DetailsOfTripActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsOfTripBinding
    private lateinit var bund : Bundle
    private lateinit var dest : DestinationDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsOfTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bund = intent.extras
        if (bund != null) {
            dest = DestinationDataModel(bund.getSerializable("alpha_3").toString(),bund.getSerializable("city").toString(),bund.getSerializable("imageUrl").toString(), bund.getSerializable("locCode").toString(), bund.getSerializable("state").toString(), )
        }

        binding.cityName.text = dest.city
        binding.stateName.text = dest.state

        // TODO: add info covid, weather etc

    }

}