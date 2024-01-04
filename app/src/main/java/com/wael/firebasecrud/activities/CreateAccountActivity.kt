package com.wael.firebasecrud.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityCreateAccountBinding
import com.wael.firebasecrud.models.Student
import com.wael.firebasecrud.utils.Constants
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val auth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()

        binding.createbtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.emailname.text.toString())||
                TextUtils.isEmpty(binding.firstname.text.toString())||
                TextUtils.isEmpty(binding.lastname.text.toString())||
                TextUtils.isEmpty(binding.password.text.toString())||
                TextUtils.isEmpty(binding.confirmPassword.text.toString())){
                Toast.makeText(this,"Please fill the form correctly",Toast.LENGTH_LONG).show()
            }else if (!binding.password.text.toString().equals(binding.confirmPassword.text.toString())) {
                Toast.makeText(this,"Confirm password doesn't match",Toast.LENGTH_LONG).show()
            }else{
                binding.loading.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(binding.emailname.text.toString(), binding.password.text.toString()).addOnCompleteListener {
                    binding.loading.visibility = View.VISIBLE
                    if( it.isSuccessful){
                        var student = Student("","",binding.firstname.text.toString(),binding.lastname.text.toString(),
                            binding.emailname.text.toString(),binding.password.text.toString())
                        saveUserToDatabase(student)
                    }else{
                        Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,SigninActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,SigninActivity::class.java))
        finish()
    }

    private fun setup() {
        sharedPreferencesHelper = SharedPreferencesHelper(this@CreateAccountActivity)
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    //save record in database
    private fun saveUserToDatabase(student: Student) {
        val usersReference = databaseReference.child("students")
        val userKey = usersReference.push().key ?: return
        student.id = userKey
        usersReference.child(userKey).setValue(student)
            .addOnSuccessListener {
                Toast.makeText(this,"Account created successfully",Toast.LENGTH_LONG).show()
                sharedPreferencesHelper.saveBoolData(Constants.ISLOGGEDIN,true)
                sharedPreferencesHelper.saveStringData("email",student.email)
                sharedPreferencesHelper.saveStringData("name","${student.fname} ${student.lname}")
                sharedPreferencesHelper.saveStringData("id",student.id)
                startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show()
            }
    }
}