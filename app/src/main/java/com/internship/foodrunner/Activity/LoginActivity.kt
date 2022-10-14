package com.internship.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.internship.foodrunner.R

class LoginActivity : AppCompatActivity() {

    lateinit var etloginMobileNumber: EditText
    lateinit var etloginPassword: EditText
    lateinit var btnLogin: Button
    lateinit var loginForgotPassword: TextView
    lateinit var loginSignUp: TextView

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Login"

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

        setContentView(R.layout.activity_login)

        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, LoginInfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        etloginMobileNumber = findViewById(R.id.etloginMobileNumber)
        etloginPassword = findViewById(R.id.etloginPassword)
        btnLogin = findViewById(R.id.btnLogin)
        loginForgotPassword = findViewById(R.id.loginForgotPassword)
        loginSignUp = findViewById(R.id.loginSignUp)

        btnLogin.setOnClickListener {
            if (!isRegistered) {
                Toast.makeText(
                    this@LoginActivity,
                    "Register first",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val mobileNumber = etloginMobileNumber.text.toString()
                val password = etloginPassword.text.toString()

                val validMobileNumber = sharedPreferences.getString("MobileNumber", "")
                var validPassword = sharedPreferences.getString("Password", "")

                if (validMobileNumber == mobileNumber && validPassword == password) {
                    savePreferences()
                    val intent = Intent(this@LoginActivity, LoginInfoActivity::class.java);
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Enter valid credentials",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        loginForgotPassword.setOnClickListener{
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginSignUp.setOnClickListener {
            if (isRegistered) {
                Toast.makeText(
                    this@LoginActivity,
                    "Already Registered, please log in",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java);
                startActivity(intent)
            }
        }
    }

    fun savePreferences() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}