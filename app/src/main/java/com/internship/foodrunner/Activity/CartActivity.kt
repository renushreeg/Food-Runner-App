package com.internship.foodrunner.Activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
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
import com.internship.foodrunner.Adapter.CartRecyclerAdapter
import com.internship.foodrunner.ConnectionManager
import com.internship.foodrunner.R
import com.internship.foodrunner.model.CartItem
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CartActivity : AppCompatActivity() {
    lateinit var recyclerMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnCart: Button
    lateinit var txtRestaurantName: TextView

    lateinit var toolbar: Toolbar

    lateinit var recyclerAdapter: CartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val jsonParams = JSONObject(intent.getStringExtra("cartList"))
        val jsonSelectedMenuList = JSONArray(intent.getStringExtra("selectedMenuList"))
        var cost = intent.getStringExtra("totalCost")

        val selectedMenuList = ArrayList<CartItem>()
        for (i in 0 until jsonSelectedMenuList.length()) {
            selectedMenuList.add(CartItem.convertFromJsonObject(jsonSelectedMenuList.get(i) as JSONObject))
        }
        println(selectedMenuList)

        val restaurantName = intent.getStringExtra("restaurantName")
        var restaurantId = jsonParams.get("restaurant_id")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"

        recyclerMenu = findViewById(R.id.recyclerMenu)
        btnCart = findViewById(R.id.btnCart)
        txtRestaurantName = findViewById(R.id.txtRestaurantName)

        txtRestaurantName.text = restaurantName
        btnCart.text = "Place order(Total Rs.$cost)"

        layoutManager = LinearLayoutManager(this@CartActivity)
        recyclerAdapter = CartRecyclerAdapter(this@CartActivity, selectedMenuList)
        recyclerMenu.adapter = recyclerAdapter
        recyclerMenu.layoutManager = layoutManager

        val queue = Volley.newRequestQueue(this@CartActivity)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        btnCart.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@CartActivity)) {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val itt = it.getJSONObject("data");
                            val success = itt.getBoolean("success")
                            if (success) {
                                val intent = Intent(this@CartActivity, SuccessActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@CartActivity,
                                    "Some Error Occured!!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@CartActivity,
                                "Some unexpected error occured!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }, Response.ErrorListener {
                        if (this@CartActivity != null) {
                            Toast.makeText(
                                this@CartActivity,
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
                val dialog = AlertDialog.Builder(this@CartActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@CartActivity?.finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    // closes the app
                    ActivityCompat.finishAffinity(this@CartActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}