package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding
import com.art.travelsombrero.databinding.ActivityModifyTripBinding
import com.squareup.picasso.Picasso
import java.net.URL

class ModifyTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyTripBinding
    private lateinit var dest : DestinationDataModel
    private lateinit var trip : TripDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModifyTripBinding.inflate(layoutInflater)
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
            trip = TripDataModel(extras.getSerializable("tripname").toString(),
                extras.getSerializable("depdate").toString(),
                extras.getSerializable("retdate").toString(),
                extras.getSerializable("city").toString())
            val modifyTripFragment= ModifyTripFragment(dest, trip)
            replaceFragment(modifyTripFragment,"boh")
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.modify_trip_frame_container, fragment, tag)
            transaction.commit()
        }
    }
}