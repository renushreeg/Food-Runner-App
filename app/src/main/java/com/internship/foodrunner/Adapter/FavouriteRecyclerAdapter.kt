package com.internship.foodrunner.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internship.foodrunner.R
import com.internship.foodrunner.database.FoodEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val foodList: List<FoodEntity>) :
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodName)
        val txtFoodPrice: TextView = view.findViewById(R.id.txtFoodPrice)
        val ic_favourite: TextView = view.findViewById(R.id.ic_favourite)
        val txtFoodRating: TextView = view.findViewById(R.id.txtFoodRating)
        val imgFoodImage: ImageView = view.findViewById(R.id.imgFoodImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row, parent, false)

        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val food = foodList[position]
        holder.txtFoodName.text = food.foodName
        holder.txtFoodRating.text = food.foodRating
        holder.txtFoodPrice.text = food.foodCostForOne
        Picasso.get().load(food.foodImage).error(R.drawable.profile).into(holder.imgFoodImage)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}