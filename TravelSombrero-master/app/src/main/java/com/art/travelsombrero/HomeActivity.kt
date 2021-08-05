package com.art.travelsombrero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.art.travelsombrero.databinding.ActivityHomeBinding
import com.art.travelsombrero.fragments.settings
import com.art.travelsombrero.fragments.newTrip
import com.art.travelsombrero.fragments.trip
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(){

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    private val nuovoFragment = newTrip()
    private val impostazioniFragment = settings()
    private val viaggiFragment = trip()


    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(nuovoFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_nuovo -> replaceFragment(nuovoFragment)
                R.id.ic_impostazioni -> replaceFragment(impostazioniFragment)
                R.id.ic_viaggi -> replaceFragment(viaggiFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }

    }
}
