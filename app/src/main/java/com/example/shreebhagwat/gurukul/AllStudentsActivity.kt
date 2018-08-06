package com.example.shreebhagwat.gurukul

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_all_users.*
import kotlinx.android.synthetic.main.all_students.view.*

class AllUsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)




        fetchStudents()
    }

    private fun fetchStudents(){
       val stuRef = FirebaseDatabase.getInstance().getReference("users/students")
            stuRef.addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()

                    p0.children.forEach{
                        it.toString()
                        Log.d("AllUsersActivity", it.toString())
                        val students = it.getValue(Students::class.java)
                        if (students != null){
                            adapter.add(StudentsItem(students))
                        }

                    }
                    adapter.setOnItemClickListener { item, view ->
                        val intent = Intent(view.context, StudentInfo::class.java)
                        startActivity(intent)

                    }

                    recycleview_alllusers.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

    }




    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.create_new_student -> {
                val intent = Intent(this, StudentsDetail::class.java)
                startActivity(intent)

            }

            R.id.log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

}

class StudentsItem(val student: Students): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Will be called in list for each of the student object
        viewHolder.itemView.studentName_textview_Allusers.text = student.studentsName

        Picasso.get().load(student.studentImageUrl).into(viewHolder.itemView.imageview_AllUsers)
    }
    override fun getLayout(): Int {
        return R.layout.all_students
    }


}

