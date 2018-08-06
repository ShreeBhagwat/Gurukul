package com.example.shreebhagwat.gurukul

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        register_button_register.setOnClickListener {
          performRegister()
        }

        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity","Open image picker")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhoto: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null ){
            // proceed and check what the selector image was
            Log.d("RegisterActivity","Photo was selected")

            selectedPhoto = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)

            select_photoImgaeView_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f

//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }


    private fun performRegister(){
        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please Enter email and Password",Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity","Email is: "+ email)
        Log.d("RegisterActivity","password is: $password")

        //Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("RegisterActivity","Successfuly created user with uid: ${it.result.user.uid}")
                    Toast.makeText(this,"Successfully Created User",Toast.LENGTH_LONG).show()

                    uploadImageToFirebaseStorage()
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","Failed to create User: ${it.message}")
                    Toast.makeText(this,"Failed To create User: ${it.message}",Toast.LENGTH_LONG).show()
                }
    }

    private fun uploadImageToFirebaseStorage(){
        if (selectedPhoto == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhoto!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity","successfully uploaded to Storage :${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d("RegisterAcvtivity", "File location $it")
                        saveUserToFirebaseDatabase(it.toString())
                    }

                }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val users = User(uid, username_edittext_registration.text.toString(), profileImageUrl)

        ref.setValue(users)
                .addOnSuccessListener {
                    Log.d("RecentActivity","User Saved To Firebase Database")

                    val intent = Intent(this, MainScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","Failed To save User To Firebase ${it.message}")
                }

    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)
