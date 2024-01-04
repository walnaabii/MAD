package com.wael.firebasecrud.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityAddMealBinding
import com.wael.firebasecrud.models.Meal
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class AddMealActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddMealBinding
    private lateinit var databaseReference: DatabaseReference
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    var mealType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setup()
        setupSideNavbar()

        databaseReference = FirebaseDatabase.getInstance().reference
        binding.add.setOnClickListener {
            if (binding.meal.text.isEmpty() || binding.calories.text.isEmpty() || mealType == "") {
                Toast.makeText(applicationContext, "Please fill values", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val meal = Meal(binding.meal.text.toString(), Integer.parseInt(binding.calories.text.toString()), mealType, System.currentTimeMillis())
            saveMealToDatabase(meal)
        }
        typeSelector()
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

    fun typeSelector() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedValue = parentView.getItemAtPosition(position).toString()
                mealType = selectedValue
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
    }

    private fun saveMealToDatabase(meal: Meal) {
        val mealsReference = databaseReference.child("meals")
        val mealKey = mealsReference.push().key ?: return
        mealsReference.child(mealKey).setValue(meal)
            .addOnSuccessListener {
                Toast.makeText(this, "Meal added successfully", Toast.LENGTH_LONG).show()
                binding.meal.setText("")
                binding.calories.setText("")
                mealType = ""
            }
            .addOnFailureListener {
                Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show()
            }
    }
}
