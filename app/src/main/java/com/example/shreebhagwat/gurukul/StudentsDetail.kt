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
import kotlinx.android.synthetic.main.activity_students_detail.*
import java.util.*

class StudentsDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_detail)

        supportActionBar?.title = "Add Students"

        st_submit.setOnClickListener {
            performNewStudentRegistrationToFirebase()
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
    private fun performNewStudentRegistrationToFirebase(){
        val studentsName = student_full_name.text.toString()
        val fathersName = st_fathers_full_name.text.toString()
        val mothersName = st_Mothers_Name.text.toString()
        val address = st_address.text.toString()
        val phoneNumber = st_phone_number.text.toString()
        val whatsApp = st_whatsApp_phone_number.text.toString()
        val sex = st_male_female.text.toString()
        val dateOfBirth = st_date_of_birth.text.toString()
        val previousSchoolName = st_previous_school.text.toString()
        val branchName = st_branch_name.text.toString()
        val standard = st_standard.text.toString()
        val fees = st_fees.text.toString()
        val noOfInstallment = st_installments.text.toString()
        val studentsNameNoSpace = studentsName.replace("\\s".toRegex(), "")
        val email = studentsNameNoSpace + "@gmail.com"
        val password = studentsNameNoSpace

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)

                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("RegisterActivity","Successfuly created Student with uid: ${it.result.user.uid}")
                    Toast.makeText(this,"Successfully Created User", Toast.LENGTH_LONG).show()
                    uploadImageToFirebaseStorage()

                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","Failed to create User: ${it.message}")
                    Toast.makeText(this,"Failed To create User: ${it.message}", Toast.LENGTH_LONG).show()
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
                        saveStudentToFirebaseDatabase(it.toString())
                    }

                }
    }
    private fun saveStudentToFirebaseDatabase(studentImageUrl:String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val stuRef = FirebaseDatabase.getInstance().getReference("/users/students/$uid")

//        val studentsName = student_full_name.text.toString()
//        val studentsNameNoSpace = studentsName.replace("\\s".toRegex(), "")
//        val email = studentsNameNoSpace + "@gmail.com"
//        val password = studentsNameNoSpace

        val students = Students(uid,
                student_full_name.text.toString(),
        st_fathers_full_name.text.toString(),
        st_Mothers_Name.text.toString(),
        st_address.text.toString(),
        st_phone_number.text.toString(),
        st_whatsApp_phone_number.text.toString(),
        st_male_female.text.toString(),
        st_date_of_birth.text.toString(),
        st_previous_school.text.toString(),
        st_branch_name.text.toString(),
        st_standard.text.toString(),
        st_fees.text.toString(),
        st_installments.text.toString(),
        studentImageUrl)

        stuRef.setValue(students)
                .addOnSuccessListener {
                    val intent = Intent(this, AllUsersActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)


                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","Failed To save Students To Firebase ${it.message}")
                }
    }
}
class Students(val uid: String,
               val studentsName:String,
               val fathersName:String,
               val mothersName:String,
               val address: String,
               val phoneNumber: String,
               val whatsApp:String,
               val sex :String,
               val dateOfBirth:String,
               val previousSchoolName:String,
               val branchName:String,
               val standardO :String,
               val fees:String,
               val noOfInstallment:String,
               val studentImageUrl: String){
    constructor(): this("","","","","","",
                       "","","","","","","",
                        "","")
}
