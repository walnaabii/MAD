package com.wael.firebasecrud.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityEditBinding
import com.wael.firebasecrud.models.Student
import com.wael.firebasecrud.utils.Constants
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class EditActivity : AppCompatActivity() {
    //variable declaration
    lateinit var binding: ActivityEditBinding
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var databaseReference: DatabaseReference

    var student = Student("","","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()

        binding.createbtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.userId.text.toString())||
                TextUtils.isEmpty(binding.firstname.text.toString())||
                TextUtils.isEmpty(binding.lastname.text.toString())){
                Toast.makeText(this,"Please fill the form correctly",Toast.LENGTH_LONG).show()
            }else{
                binding.loading.visibility = View.VISIBLE
                student.userId = binding.userId.text.toString()
                student.fname = binding.firstname.text.toString()
                student.lname = binding.lastname.text.toString()
                saveUserToDatabase(student)
            }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setup(){
        sharedPreferencesHelper = SharedPreferencesHelper(this@EditActivity)
        databaseReference = FirebaseDatabase.getInstance().reference

        readUserByUseremail(sharedPreferencesHelper.getStringData("email",""))
    }
    private fun saveUserToDatabase(student: Student) {
        val usersReference = databaseReference.child("students")
        usersReference.child(student.id).setValue(student)
            .addOnSuccessListener {
                binding.loading.visibility = View.GONE
                Toast.makeText(this,"updated successfully",Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show()
            }
    }
    private fun readUserByUseremail(useremail: String) {
        if (useremail.isEmpty()){
            Toast.makeText(this,"User not found",Toast.LENGTH_LONG).show()
            return
        }
        binding.loading.visibility = View.VISIBLE
        val usersReference = databaseReference.child("students")

        val query = usersReference.orderByChild("email").equalTo(useremail)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.loading.visibility = View.GONE
                for (userSnapshot in dataSnapshot.children) {
                    student = userSnapshot.getValue(Student::class.java)!!

                }
                binding.userId.setText(student.userId?:"")
                binding.firstname.setText(student.fname?:"")
                binding.lastname.setText(student.lname?:"")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.loading.visibility = View.GONE
            }
        })
    }
}