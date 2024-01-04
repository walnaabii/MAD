package com.wael.firebasecrud.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivitySigninBinding
import com.wael.firebasecrud.models.Student
import com.wael.firebasecrud.utils.Constants
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class SigninActivity : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper = SharedPreferencesHelper(this@SigninActivity)
        binding.signup.setOnClickListener {
            startActivity(Intent(this,CreateAccountActivity::class.java))
            finish()
        }
        binding.login.setOnClickListener {
            if (TextUtils.isEmpty(binding.emailname.text.toString())||
                TextUtils.isEmpty(binding.password.text.toString())){
                Toast.makeText(this,"Please fill form correctly", Toast.LENGTH_LONG).show()
            }else{
                binding.loading.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(binding.emailname.text.toString(), binding.password.text.toString()).addOnCompleteListener {
                    readUserByUseremail(binding.emailname.text.toString())

                }
            }
        }
    }
    private fun readUserByUseremail(useremail: String) {
        var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        binding.loading.visibility = View.VISIBLE
        val usersReference = databaseReference.child("students")
        val query = usersReference.orderByChild("email").equalTo(useremail)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.loading.visibility = View.GONE
                var student:Student= Student()
                for (userSnapshot in dataSnapshot.children) {
                    student = userSnapshot.getValue(Student::class.java)!!
                }
                sharedPreferencesHelper.saveBoolData(Constants.ISLOGGEDIN,true)
                sharedPreferencesHelper.saveStringData("email",binding.emailname.text.toString())
                sharedPreferencesHelper.saveStringData("name","${student.fname} ${student.lname}")
                sharedPreferencesHelper.saveStringData("id",student.id)
                startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                finish()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.loading.visibility = View.GONE
            }
        })
    }
}