package com.internship.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.internship.foodrunner.R
import kotlinx.android.synthetic.main.activity_sign_up_info.*

class SignUpInfoActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Info Page"

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_sign_up_info)

        var name = sharedPreferences.getString("Name", "Your name")
        var email = sharedPreferences.getString("Email", "Email")
        var mobileNumber = sharedPreferences.getString("MobileNumber", "MobileNumber")
        var deliveryAddress = sharedPreferences.getString("DeliveryAddress", "DeliveryAddress")

        SignUpInfoName.text = "Name: ${name}"
        SignUpInfoEmail.text = "Email address: ${email}"
        SignUpInfoMobileNumber.text = "MobileNumber: ${mobileNumber}"
        SignUpInfoDeliveryAddress.text = "Delivery address: ${deliveryAddress}"
    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(this@SignUpInfoActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}