package com.example.shreebhagwat.gurukul

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        register_button_register.setOnClickListener {
          performRegister()
        }
    }
    private fun performRegister(){
        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please Enter email and Password",Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity","Email is: "+ email)
        Log.d("MainActivity","password is: $password")

        //Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("Main","Successfuly created user with uid: ${it.result.user.uid}")
                    Toast.makeText(this,"Successfully Created User",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Log.d("Main","Failed to create User: ${it.message}")
                    Toast.makeText(this,"Failed To create User: ${it.message}",Toast.LENGTH_LONG).show()
                }
    }
}
