package com.example.shreebhagwat.gurukul

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class StudentsDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_detail)

        supportActionBar?.title = "Add Students"
    }
}
