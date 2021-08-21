package com.art.travelsombrero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.art.travelsombrero.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHomeBinding

    private val newTripFragment = NewTripFragment()
    private val settingsFragment = SettingsFragment()
    private val tripsFragment = TripFragment()


    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(newTripFragment,"new_trip_fragment")

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_nuovo -> replaceFragment(newTripFragment, "new_trip_fragment")
                R.id.ic_impostazioni -> replaceFragment(settingsFragment, "settings_fragment")
                R.id.ic_viaggi -> replaceFragment(tripsFragment, "trip_fragment")
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, fragment, tag)
            transaction.commit()
        }

    }
}
