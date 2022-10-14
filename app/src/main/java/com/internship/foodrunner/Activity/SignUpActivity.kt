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
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internship.foodrunner.ConnectionManager
import com.internship.foodrunner.R
import kotlinx.android.synthetic.main.activity_login_info.*
import org.json.JSONObject
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {

    lateinit var etSignUpName: EditText
    lateinit var etSignUpEmail: EditText
    lateinit var etSignUpMobileNumber: EditText
    lateinit var etSignUpDelivery: EditText
    lateinit var etSignUpPassword: EditText
    lateinit var etSignUpConfirmPassword: EditText
    lateinit var btnRegister: Button

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Register Yourself"

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etSignUpName = findViewById(R.id.etSignUpName)
        etSignUpEmail = findViewById(R.id.etSignUpEmail)
        etSignUpMobileNumber = findViewById(R.id.etSignUpMobileNumber)
        etSignUpDelivery = findViewById(R.id.etSignUpDelivery)
        etSignUpPassword = findViewById(R.id.etSignUpPassword)
        etSignUpConfirmPassword = findViewById(R.id.etSignUpConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val Name = etSignUpName.text.toString()
            val Email = etSignUpEmail.text.toString()
            val MobileNumber = etSignUpMobileNumber.text.toString()
            val DeliveryAddress = etSignUpDelivery.text.toString()
            val password = etSignUpPassword.text.toString()
            val confirmPassword = etSignUpConfirmPassword.text.toString()

            if (password != confirmPassword || MobileNumber.length != 10) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Enter valid credentials",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                sharedPreferences.edit().putString("Name", Name).apply()
                sharedPreferences.edit().putString("Email", Email).apply()
                sharedPreferences.edit().putString("MobileNumber", MobileNumber).apply()
                sharedPreferences.edit().putString("DeliveryAddress", DeliveryAddress).apply()
                sharedPreferences.edit().putString("Password", password).apply()

                val queue = Volley.newRequestQueue(this@SignUpActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("name", Name)
                jsonParams.put("mobile_number", MobileNumber)
                jsonParams.put("password", password)
                jsonParams.put("address", DeliveryAddress)
                jsonParams.put("email", Email)

                if (ConnectionManager().checkConnectivity(this@SignUpActivity)) {
                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {
                                val itt = it.getJSONObject("data");
                                val success = itt.getBoolean("success")
                                if (success) {
                                    val ittt = itt.getJSONObject("data");
                                    sharedPreferences.edit().putString("user_id", ittt.getString("user_id")).apply()
                                    sharedPreferences.edit().putBoolean("isRegistered", true).apply()
                                    println("success: $success")
                                    val intent =
                                        Intent(this@SignUpActivity, LoginActivity::class.java);
                                    startActivity(intent)
                                } else {
                                    sharedPreferences.edit().putBoolean("isRegistered", false).apply()
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Some Error Occured!!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    println("1 error is $it")
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Some Error Occured!!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                println("2 error is $it")
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@SignUpActivity,
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
                    val dialog = AlertDialog.Builder(this@SignUpActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection not Found")
                    dialog.setPositiveButton("Open Settings") { text, listerner ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        this@SignUpActivity?.finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        // closes the app
                        ActivityCompat.finishAffinity(this@SignUpActivity)
                    }
                    dialog.create()
                    dialog.show()
                }

                val intent = Intent(this@SignUpActivity, LoginActivity::class.java);
                startActivity(intent)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}