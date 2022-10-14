package com.internship.foodrunner.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.navigation.NavigationView
import com.internship.foodrunner.Activity.LoginActivity
import com.internship.foodrunner.Activity.LoginInfoActivity
import com.internship.foodrunner.R
import com.internship.foodrunner.fragment.*
import kotlinx.android.synthetic.main.activity_login_info.*


class LogoutFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logout, container, false)

        val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Confirmation")
                dialog.setMessage("Are you sure you want to log out?")
                dialog.setPositiveButton("NO") { text, listener ->
                    val intent = Intent(getActivity(), LoginInfoActivity::class.java)
                    startActivity(intent)
                }
                dialog.setNegativeButton("YES") { text, listener ->
                    var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
                    val editor = sharedPreferences.edit()
                    editor.clear();
                    editor.commit();
                    val intent = Intent(getActivity(), LoginActivity::class.java)
                    startActivity(intent)
                }
                dialog.create()
                dialog.show()

        return view
    }
}