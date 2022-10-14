package com.internship.foodrunner.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.internship.foodrunner.R

class SuccessActivity : AppCompatActivity() {
    lateinit var btnOK: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        btnOK = findViewById(R.id.btnOK)

        btnOK.setOnClickListener {
            val intent = Intent(this@SuccessActivity, LoginInfoActivity::class.java)
            startActivity(intent)
        }
    }
}