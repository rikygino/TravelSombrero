package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding
import com.squareup.picasso.Picasso
import java.net.URL

class DetailsOfTripActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsOfTripBinding
    private lateinit var dest : DestinationDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsOfTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StrictMode.enableDefaults()

        var extras = intent.extras
        if (extras != null) {
            dest = DestinationDataModel(
                extras.getSerializable("alpha_3").toString(),
                extras.getSerializable("city").toString(),
                extras.getSerializable("farnesina").toString(),
                extras.getSerializable("food").toString(),
                extras.getSerializable("imageUrl").toString(),
                extras.getSerializable("lat").toString(),
                extras.getSerializable("locCode").toString(),
                extras.getSerializable("lon").toString(),
                extras.getSerializable("news").toString(),
                extras.getSerializable("state").toString(),
                extras.getSerializable("turistic").toString())
            val detailsOfTripFragment= DetailsOfTripFragment(dest)
            replaceFragment(detailsOfTripFragment,"boh")
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.detail_of_trip_frame_container, fragment, tag)
            transaction.commit()
        }
    }
}
