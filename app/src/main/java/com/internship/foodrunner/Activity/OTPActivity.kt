package com.internship.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internship.foodrunner.ConnectionManager
import com.internship.foodrunner.R
import org.json.JSONObject
import java.lang.Exception

class OTPActivity : AppCompatActivity() {
    lateinit var etOTP: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnForgotSubmit: Button

    lateinit var toolbar: Toolbar

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        etOTP = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnForgotSubmit = findViewById(R.id.btnForgotSubmit)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forgot Password"

        var mobileNumber = sharedPreferences.getString("MobileNumber", "MobileNumber")

        btnForgotSubmit.setOnClickListener{
            var otp = etOTP.text.toString()
            var newPassword = etNewPassword.text.toString()
            var confirmPassword = etConfirmPassword.text.toString()

            if(newPassword != confirmPassword) {
                Toast.makeText(
                    this@OTPActivity,
                    "Enter valid password",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            else {
                val queue = Volley.newRequestQueue(this@OTPActivity)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", mobileNumber)
                jsonParams.put("password", newPassword)
                jsonParams.put("otp", otp)

                if (ConnectionManager().checkConnectivity(this@OTPActivity)) {
                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {
                                val itt = it.getJSONObject("data");
                                val success = itt.getBoolean("success")
                                if (success) {
                                    sharedPreferences.edit().putString("Password", newPassword).apply()
                                    val intent =
                                        Intent(
                                            this@OTPActivity,
                                            LoginActivity::class.java
                                        );
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this@OTPActivity,
                                        "Some Error Occured!!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    println("1 error is $it")
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@OTPActivity,
                                    "Some Error Occured!!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                println("2 error is $it")
                                println("exception is $e")
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@OTPActivity,
                                "Volley Error Occured!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            println(" 3 error is $it")
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "token"
                            return headers
                        }
                    }
                    queue.add(jsonRequest)
                } else {
                    val dialog = AlertDialog.Builder(this@OTPActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection not Found")
                    dialog.setPositiveButton("Open Settings") { text, listerner ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        this@OTPActivity?.finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        // closes the app
                        ActivityCompat.finishAffinity(this@OTPActivity)
                    }
                    dialog.create()
                    dialog.show()
                }
            }
        }

        Toast.makeText(
            this@OTPActivity,
            "Enter the received OTP at your mail",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}