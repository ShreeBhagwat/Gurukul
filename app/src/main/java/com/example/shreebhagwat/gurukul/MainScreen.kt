package com.example.shreebhagwat.gurukul

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        supportActionBar?.title = "Gurukul Kids"

        verifyUsedLoggedIn()



        search_students.setOnClickListener {
            navigateToStudentScreen()
        }
    }

private fun verifyUsedLoggedIn() {
    val uid = FirebaseAuth.getInstance().uid
    if (uid == null) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
private fun navigateToStudentScreen(){
    val intent = Intent(this, AllUsersActivity::class.java)
    startActivity(intent)
}
private fun navigateToTeacherScreen(){

}
}
