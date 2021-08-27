package com.art.travelsombrero

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.art.travelsombrero.databinding.ActivityDefaultLuggageBinding
import com.art.travelsombrero.databinding.ActivityLuggageBinding

class DefaultLuggageActivity : AppCompatActivity() {

    var tripname=""
    private lateinit var binding: ActivityDefaultLuggageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var extras = intent.extras
        if (extras != null) {
            tripname = extras.getSerializable("tripname").toString()
        }
        val defaultLuggageFragment = DefaultLuggageFragment(tripname)
        binding = ActivityDefaultLuggageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(defaultLuggageFragment,"default_luggage_fragment")
    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.default_luggage_frame_container, fragment, tag)
            transaction.commit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Return to home")
        builder.setMessage("Are you sure want to return to home? Your trip will be saved ")
        builder.setPositiveButton("Confirm") { dialog, which ->
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
        }
        builder.setOnCancelListener {
        }
        builder.show()
        return true
    }

}