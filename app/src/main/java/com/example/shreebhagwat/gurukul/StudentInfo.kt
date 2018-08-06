package com.example.shreebhagwat.gurukul

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class StudentInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_info)

        supportActionBar?.title = "Student Info"
    }
}
