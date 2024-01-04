package com.wael.firebasecrud.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityMainBinding
import com.wael.firebasecrud.models.MealLog
import com.wael.firebasecrud.models.Workout
import com.wael.firebasecrud.utils.SharedPreferencesHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    //variable declaration
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setup()
        setupSideNavbar()

        binding.moreBtn.setOnClickListener {
            showPopupMenu(it)
        }
        binding.addMeal.setOnClickListener {
            startActivity(Intent(this, MealsActivity::class.java))
        }
        binding.addworkout.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        readCaloriesFun(sharedPreferencesHelper.getStringData("id", ""))
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
        sharedPreferencesHelper = SharedPreferencesHelper(this@MainActivity)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val currentDate: String = dateFormat.format(Date())
        binding.dateview.text = currentDate
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.editBtn -> {
                    startActivity(Intent(this, EditActivity::class.java))
                    true
                }
                R.id.contactUs -> {
                    true
                }
                R.id.singout -> {
                    signOut()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun signOut() {
        auth.signOut()
        sharedPreferencesHelper.clearData()
        startActivity(Intent(this, SigninActivity::class.java))
        finish()
    }

    private fun readCaloriesFun(userId: String) {
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
                var workoutcheck = true
                for (userSnapshot in dataSnapshot.children.reversed()) {
                    workout = userSnapshot.getValue(Workout::class.java)!!
                    if (userId == workout.userId && areSameDay(workout.date, System.currentTimeMillis())) {
                        workoutcheck = false
                        workout = userSnapshot.getValue(Workout::class.java)!!
                        readMealLogFun(userId, workout)
                        break
                    }
                }
                if (workoutcheck) {
                    binding.contentlayout.visibility = View.GONE
                    binding.addMeal.visibility = View.GONE
                    binding.addworkout.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.contentlayout.visibility = View.GONE
                binding.addMeal.visibility = View.GONE
                binding.addworkout.visibility = View.VISIBLE
            }
        })
    }

    private fun readMealLogFun(userId: String, workout: Workout) {
        if (userId.isEmpty()) {
            Toast.makeText(this, "MealLog not found", Toast.LENGTH_LONG).show()
            return
        }

        val usersReference = databaseReference.child("mealLogs")
        val query = usersReference
            .orderByChild("date")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var mealLogList: MutableList<MealLog> = mutableListOf()

                for (userSnapshot in dataSnapshot.children.reversed()) {
                    val mealLog = userSnapshot.getValue(MealLog::class.java)!!
                    if (userId == mealLog.userId && areSameDay(mealLog.date, System.currentTimeMillis())) {
                        mealLogList.add(mealLog)
                    }
                }
                val totalConsumedCal =
                    workout.cardioCal + workout.weightCal + workout.cyclingCal + workout.runningCal + workout.walkingCal
                if (mealLogList.isNotEmpty()) {
                    binding.gained.text = "${mealLogList.sumOf { it.calories }.toString()}\ncal"
                    binding.remaining.text =
                        "${mealLogList.sumOf { it.calories } - totalConsumedCal}\ncal"
                    binding.cyclingCal.text = workout.cyclingCal.toString() + "\ncal"
                    binding.walkingCal.text = workout.walkingCal.toString() + "\ncal"
                    binding.runningCal.text = workout.runningCal.toString() + "\ncal"
                    binding.cardio.text = workout.cardioCal.toString() + "\ncal"
                    binding.weight.text = workout.weightCal.toString() + "\ncal"

                    binding.contentlayout.visibility = View.VISIBLE
                    binding.addMeal.visibility = View.GONE
                    binding.addworkout.visibility = View.GONE
                } else {
                    binding.contentlayout.visibility = View.GONE
                    binding.addMeal.visibility = View.VISIBLE
                    binding.addworkout.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.contentlayout.visibility = View.GONE
                binding.addMeal.visibility = View.VISIBLE
                binding.addworkout.visibility = View.GONE
            }
        })
    }

    fun areSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val date1 = Date(timestamp1)
        val date2 = Date(timestamp2)

        val dateFormat = SimpleDateFormat("yyyyMMdd")

        return dateFormat.format(date1) == dateFormat.format(date2)
    }
}
