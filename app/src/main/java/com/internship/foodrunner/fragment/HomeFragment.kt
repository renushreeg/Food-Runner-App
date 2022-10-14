package com.internship.foodrunner.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internship.foodrunner.Adapter.HomeRecyclerAdapter
import com.internship.foodrunner.ConnectionManager
import com.internship.foodrunner.R
import com.internship.foodrunner.database.FoodEntity
import com.internship.foodrunner.model.Food
import org.json.JSONException

class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val foodList = arrayListOf<Food>()

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        recyclerHome = view.findViewById(R.id.recyclerHome)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try {
                        val itt = it.getJSONObject("data");
                        progressLayout.visibility = View.GONE
                        val success = itt.getBoolean("success")
                        if (success) {
                            val data = itt.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val foodJsonObject = data.getJSONObject(i)
                                val foodObject = Food(
                                    foodJsonObject.getString("id"),
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("rating"),
                                    foodJsonObject.getString("cost_for_one"),
                                    foodJsonObject.getString("image_url"),
                                )
                                foodList.add(foodObject)

                                layoutManager = LinearLayoutManager(activity)

                                recyclerAdapter = HomeRecyclerAdapter(activity as Context, foodList)

                                recyclerHome.adapter = recyclerAdapter

                                recyclerHome.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occured!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occured!!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occured!!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        println(it)
                        println("error is: $it")
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "token"
                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings") { text, listerner ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                // closes the app
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }
}