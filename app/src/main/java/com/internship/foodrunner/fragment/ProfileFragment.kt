package com.internship.foodrunner.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.internship.foodrunner.R

class ProfileFragment : Fragment() {
    lateinit var txtProfileName: TextView
    lateinit var txtProfileNumber: TextView
    lateinit var txtProfileEmail: TextView
    lateinit var txtProfileAddress: TextView

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileNumber = view.findViewById(R.id.txtProfileNumber)
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail)
        txtProfileAddress = view.findViewById(R.id.txtProfileAddress)

//        sharedPreferences =
//            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE);

        var name = sharedPreferences.getString("Name", "Your name")
        var email = sharedPreferences.getString("Email", "Email")
        var mobileNumber = sharedPreferences.getString("MobileNumber", "MobileNumber")
        var deliveryAddress = sharedPreferences.getString("DeliveryAddress", "DeliveryAddress")

        txtProfileName.text = name
        txtProfileNumber.text = email
        txtProfileEmail.text = mobileNumber
        txtProfileAddress.text = deliveryAddress

        return view
    }
}
