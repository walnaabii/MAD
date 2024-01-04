package com.wael.firebasecrud.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.wael.firebasecrud.R
import com.wael.firebasecrud.databinding.ActivitySplashBinding
import com.wael.firebasecrud.utils.Constants
import com.wael.firebasecrud.utils.SharedPreferencesHelper

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferencesHelper = SharedPreferencesHelper(this@SplashActivity)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.image.startAnimation(slideAnimation)

        Handler().postDelayed({
            if (sharedPreferencesHelper.getBoolData(Constants.ISLOGGEDIN,false)){
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(mainIntent)
            }else{
                val mainIntent = Intent(this@SplashActivity, SigninActivity::class.java)
                startActivity(mainIntent)
            }
            overridePendingTransition(0, 0)
            finish()
        }, 3000)
    }
}