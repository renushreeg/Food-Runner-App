package com.internship.foodrunner.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internship.foodrunner.R
import com.internship.foodrunner.model.CartItem

class CartRecyclerAdapter(val context: Context, private val itemList: ArrayList<CartItem>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CardViewHolder>() {

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtCartMenuItemName: TextView = view.findViewById(R.id.txtCartMenuItemName)
        val txtCartMenuItemCost: TextView = view.findViewById(R.id.txtCartMenuItemCost)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_single_row, parent, false)
        return CartRecyclerAdapter.CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val menu = itemList[position]
        holder.txtCartMenuItemName.text = menu.cartMenuItemName
        holder.txtCartMenuItemCost.text = "Rs. " + menu.cartMenuItemCost
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}