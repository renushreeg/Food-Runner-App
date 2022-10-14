package com.internship.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internship.foodrunner.Adapter.MenuRecyclerAdapter
import com.internship.foodrunner.ConnectionManager
import com.internship.foodrunner.R
import com.internship.foodrunner.model.CartItem
import com.internship.foodrunner.model.MenuItem
import com.internship.foodrunner.model.MenuList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RestaurantDetailsActivity : AppCompatActivity() {

    lateinit var recyclerMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var btnCart: Button

    lateinit var toolbar: Toolbar
    lateinit var sharedPreferences: SharedPreferences


    val menuList = arrayListOf<MenuItem>()

    lateinit var recyclerAdapter: MenuRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        var user_id = sharedPreferences.getString("user_id", "90")

        var foodName = intent.getStringExtra("foodName")
        var id = intent.getStringExtra("food_id")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = foodName

        recyclerMenu = findViewById(R.id.recyclerMenu)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        btnCart = findViewById(R.id.btnCart)

        var menuItemList = MenuList()

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this@RestaurantDetailsActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$id"

        if (ConnectionManager().checkConnectivity(this@RestaurantDetailsActivity)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try {
                        val itt = it.getJSONObject("data");
                        progressLayout.visibility = View.GONE
                        val success = itt.getBoolean("success")
                        if (success) {
                            val data = itt.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val menuItemJsonObject = data.getJSONObject(i)
                                val menuItem = MenuItem(
                                    menuItemJsonObject.getString("id"),
                                    menuItemJsonObject.getString("name"),
                                    menuItemJsonObject.getString("cost_for_one"),
                                    menuItemJsonObject.getString("restaurant_id"),
                                )
                                menuList.add(menuItem)

                                layoutManager = LinearLayoutManager(this@RestaurantDetailsActivity)

                                recyclerAdapter = MenuRecyclerAdapter(
                                    this@RestaurantDetailsActivity,
                                    menuList,
                                    menuItemList
                                )

                                recyclerMenu.adapter = recyclerAdapter

                                recyclerMenu.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(
                                this@RestaurantDetailsActivity,
                                "Some Error Occured!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@RestaurantDetailsActivity,
                            "Some unexpected error occured!!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }, Response.ErrorListener {
                    if (this@RestaurantDetailsActivity != null) {
                        Toast.makeText(
                            this@RestaurantDetailsActivity,
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
            val dialog = AlertDialog.Builder(this@RestaurantDetailsActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this@RestaurantDetailsActivity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                // closes the app
                ActivityCompat.finishAffinity(this@RestaurantDetailsActivity)
            }
            dialog.create()
            dialog.show()
        }

        btnCart.setOnClickListener {
            var foodArray = menuItemList.getMenuItem()
            var cost = menuItemList.getCost()
            var selectedMenuList = ArrayList<JSONObject>()
            for (menuItem in menuItemList.getSelectedMenuList()) {
                selectedMenuList.add(CartItem.convertToJsonObject(menuItem))
            }

            var cartList = JSONObject()
            cartList.put("user_id", user_id.toString())
            cartList.put("restaurant_id", id.toString())
            cartList.put("total_cost", cost.toString())
            cartList.put("food", foodArray)

            val intent = Intent(this@RestaurantDetailsActivity, CartActivity::class.java)
            intent.putExtra("cartList", cartList.toString())
            intent.putExtra("selectedMenuList", JSONArray(selectedMenuList).toString())
            intent.putExtra("restaurantName", foodName)
            intent.putExtra("totalCost", cost.toString())
            startActivity(intent)
        }
    }
}
