package com.wael.firebasecrud.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivityAboutUsBinding
import com.wael.firebasecrud.utils.SharedPreferencesHelper
import android.os.Bundle



class AboutUsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutUsBinding
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setup()
        setupSideNavbar()
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
}
