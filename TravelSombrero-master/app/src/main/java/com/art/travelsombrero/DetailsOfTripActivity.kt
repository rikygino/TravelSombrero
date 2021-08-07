package com.art.travelsombrero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding
import com.art.travelsombrero.databinding.ActivityHomeBinding

class DetailsOfTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsOfTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsOfTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: add info covid, weather etc

    }
}