package com.wael.firebasecrud.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wael.firebasecrud.R
import com.wael.firebasecrud.adapters.MealAdapter
import com.wael.firebasecrud.databinding.ActivityMealsBinding
import com.wael.firebasecrud.models.Meal
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class MealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityMealsBinding
    private lateinit var databaseReference: DatabaseReference
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    var mealsList: MutableList<Meal> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setup()
        setupSideNavbar()
        databaseReference = FirebaseDatabase.getInstance().reference

        binding.addMeal.setOnClickListener {
            startActivity(Intent(this, AddMealActivity::class.java))
        }
        binding.breakfast.setOnClickListener {
            Toast.makeText(applicationContext, "Breakfast meal", Toast.LENGTH_LONG).show()
            readmealsBytype("Breakfast")
        }
        binding.lunch.setOnClickListener {
            Toast.makeText(applicationContext, "Lunch meal", Toast.LENGTH_LONG).show()
            readmealsBytype("Lunch")
        }
        binding.dinner.setOnClickListener {
            Toast.makeText(applicationContext, "Dinner meal", Toast.LENGTH_LONG).show()
            readmealsBytype("Dinner")
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

        // Handle navigation item clicks
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

    override fun onResume() {
        super.onResume()
        readMealsFun()
    }

    //read meals from database
    private fun readMealsFun() {
        // Create a reference to the "meals" node in the database
        val mealsReference = databaseReference.child("meals")
        mealsList.clear()

        mealsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mealSnapshot in dataSnapshot.children) {
                    mealsList.add(mealSnapshot.getValue(Meal::class.java)!!)
                }
                binding.mealsrecyclerview.layoutManager = GridLayoutManager(this@MealsActivity, 2)
                binding.mealsrecyclerview.adapter = MealAdapter("Breakfast", mealsList, this@MealsActivity)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun readmealsBytype(type: String) {
        mealsList.clear()

        val mealReference = databaseReference.child("meals")

        val query = mealReference.orderByChild("time").equalTo(type)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mealSnapshot in dataSnapshot.children) {
                    mealsList.add(mealSnapshot.getValue(Meal::class.java)!!)
                }
                binding.mealsrecyclerview.layoutManager = GridLayoutManager(this@MealsActivity, 2)
                binding.mealsrecyclerview.adapter = MealAdapter(type, mealsList, this@MealsActivity)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}