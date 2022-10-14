package com.internship.foodrunner.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import java.util.logging.Handler
import android.os.Handler
import com.internship.foodrunner.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val startAct = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(startAct)
        }, 2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}