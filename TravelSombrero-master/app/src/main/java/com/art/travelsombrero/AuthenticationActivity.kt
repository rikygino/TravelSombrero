package com.art.travelsombrero

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.art.travelsombrero.databinding.ActivityAuthenticationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AuthenticationActivity : AppCompatActivity(){

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("143653158800-ed97u1lc4oshmujfo7v14secs4jtj7cf.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Handler(Looper.getMainLooper()).postDelayed({
            if(user != null){
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }
        }, 0)

        binding.google.setOnClickListener{
            signIn()
        }
        binding.loginButton.setOnClickListener {
            var intent = Intent( this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.registrationButton.setOnClickListener {
            var intent = Intent( this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivityTask", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("SignInActivity", "signInWithCredential:success")
                saveUserToFirebaseDatabase()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // If sign in fails, display a message to the user.
                Log.d("SignInActivity", "signInWithCredential:failure")
            }
        }
    }

    private fun saveUserToFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val refwrite = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val username = FirebaseAuth.getInstance().currentUser?.displayName
        val user = username?.let { User(uid, it) }
        refwrite.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                        var onDB = snapshot.getValue(User2::class.java)
                if (onDB != null) {
                    if (onDB.username == ""){
                        refwrite.setValue(user)
                            .addOnSuccessListener {
                                Log.d("Register Activity", "Utente salvato anche nel DB")
                                var intent = Intent(this@AuthenticationActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@AuthenticationActivity, "Impossibile salvare l'utente nel DB ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }
}

class User2(val uid:String, val username: String){
    constructor() : this("","")
}



