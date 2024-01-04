package com.wael.firebasecrud.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityWorkOutBinding
import com.wael.firebasecrud.models.Workout
import com.wael.firebasecrud.utils.SharedPreferencesHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkOutBinding
    private lateinit var databaseReference: DatabaseReference
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        databaseReference = FirebaseDatabase.getInstance().reference
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        setupSideNavbar()
        binding.addBtnLayout.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
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

    override fun onResume() {
        super.onResume()
        readWorkoutFun(sharedPreferencesHelper.getStringData("id", ""))
    }

    private fun readWorkoutFun(userId: String) {
        if (userId.isEmpty()) {
            Toast.makeText(this, "Workout not found", Toast.LENGTH_LONG).show()
            return
        }

        val usersReference = databaseReference.child("workout")
        val query = usersReference
            .orderByChild("date")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var workout: Workout = Workout()

                for (userSnapshot in dataSnapshot.children.reversed()) {
                    workout = userSnapshot.getValue(Workout::class.java)!!
                    if (userId == workout.userId && areSameDay(
                            workout.date,
                            System.currentTimeMillis()
                        )
                    ) {
                        workout = userSnapshot.getValue(Workout::class.java)!!
                        binding.finished.text = workout.finished.toString()
                        binding.remainingworkout.text =
                            (3 - workout.finished).toString() + " workout"
                        val timespent =
                            workout.cycling + workout.running + workout.walking + workout.cardio
                        binding.timeSpent.text = timespent.toString() + " mints"
                        binding.cycling.text = workout.cycling.toString() + " mints"
                        binding.running.text = workout.running.toString() + " mints"
                        binding.walking.text = workout.walking.toString() + " mints"
                        binding.cardio.text = workout.cardio.toString() + " mints"
                        binding.weight.text = workout.weight.toString() + " exercises"

                        binding.workoutlayout.visibility = View.VISIBLE
                        binding.addBtnLayout.visibility = View.GONE
                        break
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun getTodayStartTimestamp(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = Date()
        val formattedDate = dateFormat.format(todayDate)
        val startDate = dateFormat.parse(formattedDate)
        return startDate?.time ?: 0
    }

    fun areSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        // Convert timestamps to Dates
        val date1 = Date(timestamp1)
        val date2 = Date(timestamp2)

        val dateFormat = SimpleDateFormat("yyyyMMdd")

        return dateFormat.format(date1) == dateFormat.format(date2)
    }
}
