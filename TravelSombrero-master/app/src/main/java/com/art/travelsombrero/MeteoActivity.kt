package com.art.travelsombrero

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.art.travelsombrero.databinding.ActivityMeteoBinding

class MeteoActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMeteoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMeteoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //StrictMode.enableDefaults()

        val meteoFragment = MeteoFragment()
        replaceFragment(meteoFragment,"meteo_fragment")
    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.meteo_frame_container, fragment, tag)
            transaction.commit()
        }
    }
}
