package com.art.travelsombrero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.art.travelsombrero.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonRegister.setOnClickListener {
            performRegistration()
        }

        binding.alreadyHaveAccountRegister.setOnClickListener{
            Log.d( "Register Activity",  "Try to show login activity")
            var intent = Intent( this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegistration(){
        var username = binding.usernameEditTextRegister.text.toString()
        var email = binding.emailEditTextRegister.text.toString()
        var password = binding.passwordEditTextRegister.text.toString()

        //controllo che i campi non siano nulli o vuoti
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()){
            Toast.makeText(this, "Please enter text in email/password field", Toast.LENGTH_LONG).show()
            return
        }

        Log.d( "Register Activity",  "Username is $username")
        Log.d( "Register Activity",  "Email is $email")
        Log.d( "Register Activity",  "Password is $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                //se non ha funzionato
                if(!it.isSuccessful){
                    return@addOnCompleteListener
                }
                //se ha funzionato
                Log.d("Register Activity", "Utente creato con successo")
                saveUserToFirebaseDatabase()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user : ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("Register Activity", "Failed to create user : ${it.message}")
            }

    }

    private fun saveUserToFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.usernameEditTextRegister.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register Activity", "Utente salvato anche nel DB")
                var intent = Intent( this, HomeActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Impossibile salvare l'utente nel DB ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

}

class User(val uid:String, val username: String)
