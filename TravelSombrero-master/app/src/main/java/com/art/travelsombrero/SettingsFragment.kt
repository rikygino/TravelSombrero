package com.art.travelsombrero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.art.travelsombrero.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 * Use the [settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initSettings(view)
        return view
    }

    private fun initSettings(view: View){
        val username = FirebaseAuth.getInstance().currentUser?.displayName
        val email = FirebaseAuth.getInstance().currentUser?.email
        var name = view.findViewById<TextView>(R.id.nameTextView)
        var myemail = view.findViewById<TextView>(R.id.emailTextView)
        name.text = username
        myemail.text = email

        name.setOnClickListener {

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}