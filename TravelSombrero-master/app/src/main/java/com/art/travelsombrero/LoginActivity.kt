package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.art.travelsombrero.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        binding.backToRegistration.setOnClickListener {
            Log.d( "Login Activity",  "Try to show login activity")
            var intent = Intent( this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun performLogin(){

        var email = binding.emailEditTextLogin.toString()
        var password = binding.passwordEditTextLogin.toString()

        if(email.isEmpty()|| password.isEmpty()){
            Toast.makeText(this, "Please enter text in email/password field", Toast.LENGTH_LONG).show()
            return
        }

        Log.d( "Main Activity", "Email is $email")
        Log.d( "Main Activity",  "Password is $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                //se non ha funzionato
                if(!it.isSuccessful){
                    return@addOnCompleteListener
                }
                //se ha funzionato
                var intent = Intent( this, HomeActivity::class.java)
                startActivity(intent)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Login fallito : ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("Main Activity", "Login fallito: ${it.message}")
            }


    }
}