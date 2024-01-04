package com.wael.firebasecrud.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityAddWorkoutBinding
import com.wael.firebasecrud.models.Workout
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class AddWorkoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddWorkoutBinding
    private lateinit var databaseReference: DatabaseReference
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setup()
        setupSideNavbar()
        databaseReference = FirebaseDatabase.getInstance().reference
        sharedPreferencesHelper = SharedPreferencesHelper(this@AddWorkoutActivity)
        binding.submit.setOnClickListener {
            if (binding.finished.text.isEmpty() ||
                binding.cycling.text.isEmpty() ||
                binding.cyclingCal.text.isEmpty() ||
                binding.running.text.isEmpty() ||
                binding.runningCal.text.isEmpty() ||
                binding.walking.text.isEmpty() ||
                binding.walkingCal.text.isEmpty() ||
                binding.weights.text.isEmpty() ||
                binding.weightCal.text.isEmpty() ||
                binding.cardio.text.isEmpty() ||
                binding.cardioCal.text.isEmpty()
            ) {
                Toast.makeText(applicationContext, "Please fill form completely", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val totalTime = binding.walking.text.toString().toInt() +
                    binding.running.text.toString().toInt() +
                    binding.cycling.text.toString().toInt() +
                    binding.cardio.text.toString().toInt()
            val workout = Workout(
                "",
                sharedPreferencesHelper.getStringData("id", ""),
                binding.finished.text.toString().toInt(),
                3,
                totalTime,
                binding.walking.text.toString().toInt(),
                binding.walkingCal.text.toString().toInt(),
                binding.running.text.toString().toInt(),
                binding.runningCal.text.toString().toInt(),
                binding.cycling.text.toString().toInt(),
                binding.cyclingCal.text.toString().toInt(),
                binding.weights.text.toString().toInt(),
                binding.weightCal.text.toString().toInt(),
                binding.cardio.text.toString().toInt(),
                binding.cardioCal.text.toString().toInt(),
                System.currentTimeMillis()
            )
            saveWorkoutToDatabase(workout)
        }
    }

    private fun setupSideNavbar() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.navigationbar.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this, EditActivity::class.java))
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                R.id.calorieTracker -> {
                    startActivity(Intent(this, CaloriesTrackerActivity::class.java))
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                R.id.mealTracker -> {
                    startActivity(Intent(this, MealsActivity::class.java))
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                R.id.bmi -> {
                    startActivity(Intent(this, MBICalculatorActivity::class.java))
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                R.id.workout -> {
                    startActivity(Intent(this, WorkOutActivity::class.java))
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                R.id.aboutus -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                R.id.contactUs -> {
                    // Handle contact action as needed
                    binding.drawer.closeDrawer(binding.navigationbar)
                    true
                }
                else -> false
            }
        }

        val headerView = binding.navigationbar.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        val useremailTextView = headerView.findViewById<TextView>(R.id.useremail)
        usernameTextView.text = sharedPreferencesHelper.getStringData("name", "")
        useremailTextView.text = sharedPreferencesHelper.getStringData("email", "")
    }

    private fun setup() {
        sharedPreferencesHelper = SharedPreferencesHelper(this)
    }

    private fun saveWorkoutToDatabase(workout: Workout) {
        val mealsReference = databaseReference.child("workout")
        val mealKey = mealsReference.push().key ?: return
        workout.id = mealKey
        mealsReference.child(mealKey).setValue(workout)
            .addOnSuccessListener {
                Toast.makeText(this, "workout added successfully", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show()
            }
    }
}
