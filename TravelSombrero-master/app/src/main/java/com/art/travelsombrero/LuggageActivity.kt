package com.art.travelsombrero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.art.travelsombrero.databinding.ActivityHomeBinding
import com.art.travelsombrero.databinding.ActivityLuggageBinding

class LuggageActivity : AppCompatActivity() {

    var tripname=""

    private lateinit var binding: ActivityLuggageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var extras = intent.extras
        if (extras != null) {
            tripname = extras.getSerializable("tripname").toString()
        }
        val luggageFragment = LuggageFragment(tripname)
        binding = ActivityLuggageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(luggageFragment,"luggage_fragment")
    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.luggage_frame_container, fragment, tag)
            transaction.commit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "Press the home button to save your Trip and to go back!", Toast.LENGTH_LONG).show()
            false
        } else super.onKeyDown(keyCode, event)
    }
}