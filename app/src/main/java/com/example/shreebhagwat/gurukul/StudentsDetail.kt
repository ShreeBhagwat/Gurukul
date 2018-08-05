package com.example.shreebhagwat.gurukul

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_students_detail.*

class StudentsDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_detail)

        supportActionBar?.title = "Add Students"

        st_submit.setOnClickListener {
            saveStudentsDetailsToFirebase()
        }

        st_photo.setOnClickListener {
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

//            select_photoImgaeView_register.setImageBitmap(bitmap)
//            selectphoto_button_register.alpha = 0f

            val bitmapDrawable = BitmapDrawable(bitmap)
            st_photo.setBackgroundDrawable(bitmapDrawable)
            st_photo.text = null
        }
    }
    private fun saveStudentsDetailsToFirebase(){

    }
}
