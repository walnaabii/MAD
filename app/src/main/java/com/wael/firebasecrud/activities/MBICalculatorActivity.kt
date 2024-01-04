package com.wael.firebasecrud.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.wael.firebasecrud.R
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class MBICalculatorActivity : AppCompatActivity() {
    private lateinit var editTextWeight: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationbar: NavigationView

    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbicalculator)

        editTextWeight = findViewById(R.id.editTextWeight)
        editTextHeight = findViewById(R.id.editTextHeight)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        textViewResult = findViewById(R.id.textViewResult)
        toolbar = findViewById(R.id.toolbar)
        drawer = findViewById(R.id.drawer)
        navigationbar = findViewById(R.id.navigationbar)
        setSupportActionBar(toolbar)
        setup()
        setupSideNavbar()
        buttonCalculate.setOnClickListener {
            calculateBMI()
        }
    }

    private fun setupSideNavbar() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Handle navigation item clicks
        navigationbar.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile-> {
                    startActivity(Intent(this, EditActivity::class.java))
                    drawer.closeDrawer(navigationbar)
                    true
                }
                R.id.calorieTracker-> {
                    startActivity(Intent(this, CaloriesTrackerActivity::class.java))
                    drawer.closeDrawer(navigationbar)
                    true
                }
                R.id.mealTracker-> {
                    startActivity(Intent(this, MealsActivity::class.java))
                    drawer.closeDrawer(navigationbar)
                    true
                }
                R.id.bmi-> {
                    startActivity(Intent(this, MBICalculatorActivity::class.java))
                    drawer.closeDrawer(navigationbar)
                    true
                }
                R.id.workout->{
                    startActivity(Intent(this, WorkOutActivity::class.java))
                    drawer.closeDrawer(navigationbar)
                    true
                }
                R.id.aboutus->{
                    startActivity(Intent(this, AboutUsActivity::class.java))
                    drawer.closeDrawer(navigationbar)
                    true
                }
                R.id.contactUs->{
                    drawer.closeDrawer(navigationbar)
                    true
                }
                else -> false
            }
        }

        val headerView = navigationbar.getHeaderView(0)
        // Set the user name in the header
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        val useremailTextView = headerView.findViewById<TextView>(R.id.useremail)
        usernameTextView.text = sharedPreferencesHelper.getStringData("name","")
        useremailTextView.text = sharedPreferencesHelper.getStringData("email","")
    }

    private fun setup(){
        sharedPreferencesHelper = SharedPreferencesHelper(this)
    }

    private fun calculateBMI() {
        val weight = editTextWeight.text.toString().toFloatOrNull()
        val height = editTextHeight.text.toString().toFloatOrNull()

        if (weight != null && height != null) {
            val bmi = weight / (height * height)
            displayResult(bmi)
        } else {
            textViewResult.text = "Invalid input. Please enter valid weight and height."
        }
    }


    private fun displayResult(bmi: Float) {
        val resultText = "BMI: %.2f".format(bmi) // "%.2f" float should be 2 decimal.
        textViewResult.text = resultText

        val classification = getBMIClassification(bmi)
        val classificationText = "Classification: $classification"
        findViewById<TextView>(R.id.textViewClassification).text = classificationText

        val age = 30
        val bfp = calculateBFP(bmi, age)
        val bfpText = "Body Fat Percentage: %.2f%%".format(bfp)
        findViewById<TextView>(R.id.textViewBFP).text = bfpText

        val bmr = calculateBMR(editTextWeight.text.toString().toFloat(), editTextHeight.text.toString().toFloat(), age)
        val bmrText = "Basal Metabolic Rate (BMR): %.2f kcal".format(bmr)
        findViewById<TextView>(R.id.textViewBMR).text = bmrText.toString() // Convert Double to String

        val totalBodyWeight = editTextWeight.text.toString().toFloat()
        val bodyFatMass = totalBodyWeight * bfp / 100
        val lbm = totalBodyWeight - bodyFatMass
        val lbmText = "Lean Body Mass (LBM): %.2f kg".format(lbm)
        findViewById<TextView>(R.id.textViewLBM).text = lbmText
    }


        private fun calculateBMR(weight: Float, height: Float, age: Int): Float {
            return (10 * weight + 6.25 * height * 100 - 5 * age + 5).toFloat()
        }






    private fun calculateBFP(bmi: Float, age: Int): Float {
        return 1.20f * bmi + 0.23f * age - 16.2f
    }

    private fun getBMIClassification(bmi: Float): String {
        return when {
            bmi < 17 -> "Thinness"
            bmi in 17.0..18.5 -> "Underweight"
            bmi in 18.5..25.0 -> "Normal"
            bmi in 25.0..30.0 -> "Overweight"
            bmi in 30.0..35.0 -> "Obese (Class 1)"
            else -> "Obese (Class 2 or higher)"
        }
    }
}
