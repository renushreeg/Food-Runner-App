package com.internship.foodrunner.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.internship.foodrunner.Adapter.HomeRecyclerAdapter
import com.internship.foodrunner.ConnectionManager
import com.internship.foodrunner.R
import com.internship.foodrunner.database.FoodEntity
import com.internship.foodrunner.model.Food
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etForgotMobileNumber: EditText
    lateinit var etForgotEmailAddress: EditText
    lateinit var btnForgotNext: Button

    lateinit var toolbar: Toolbar

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Forgot Password"

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_forgot_password)

        etForgotEmailAddress = findViewById(R.id.etForgotEmailAddress)
        etForgotMobileNumber = findViewById(R.id.etForgotMobileNumber)
        btnForgotNext = findViewById(R.id.btnForgotNext)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forgot Password"

        btnForgotNext.setOnClickListener {
            val mobileNumber = etForgotMobileNumber.text.toString()
            val emailAddress = etForgotEmailAddress.text.toString()
            if (mobileNumber == "") {
                Snackbar.make(etForgotMobileNumber, "Enter Mobile number", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            if (emailAddress == "") {
                Snackbar.make(etForgotMobileNumber, "Enter Email address", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {

                sharedPreferences.edit().putString("Email", emailAddress).apply()
                sharedPreferences.edit().putString("MobileNumber", mobileNumber).apply()

                val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", mobileNumber)
                jsonParams.put("email", emailAddress)

                if (ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)) {
                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {
                                val itt = it.getJSONObject("data");
                                val success = itt.getBoolean("success")
                                if (success) {
                                    val intent =
                                        Intent(
                                            this@ForgotPasswordActivity,
                                            OTPActivity::class.java
                                        );
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        "Some Error Occured!!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    "Some Error Occured!!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Volley Error Occured!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
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
                    val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection not Found")
                    dialog.setPositiveButton("Open Settings") { text, listerner ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        this@ForgotPasswordActivity?.finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        // closes the app
                        ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                    }
                    dialog.create()
                    dialog.show()
                }
            }
        }
    }
}