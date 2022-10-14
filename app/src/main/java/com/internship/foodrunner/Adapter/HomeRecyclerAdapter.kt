package com.internship.foodrunner.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.internship.foodrunner.Activity.RestaurantDetailsActivity
import com.internship.foodrunner.DBAsyncTask
import com.internship.foodrunner.R
import com.internship.foodrunner.database.FoodEntity
import com.internship.foodrunner.fragment.HomeFragment
import com.internship.foodrunner.model.Food
import com.internship.foodrunner.model.MenuItem
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Food>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val food = itemList[position]
        var foodEntity: FoodEntity = FoodEntity(
            food.foodId.toInt(),
            food.foodName,
            food.foodRating,
            food.foodCostForOne,
            food.foodImage,
        );
        holder.txtFoodName.text = food.foodName
        holder.txtFoodRating.text = food.foodRating
        holder.txtFoodPrice.text = "₹" + food.foodCostForOne + "/person"
        holder.txtFavourite.setOnClickListener {
            if (!DBAsyncTask(context, foodEntity, 1).execute().get()) {
                val async =
                    DBAsyncTask(context, foodEntity, 2).execute()
                val result = async.get()
                if (result) {
                    isFavourite(holder)
                } else {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()
                }
            } else {
                val async =
                    DBAsyncTask(context, foodEntity, 3).execute()
                val result = async.get()
                if (result) {
                    isNotFavourite(holder)
                } else {
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val checkFav = DBAsyncTask(context, foodEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            isFavourite(holder)
        } else {
            isNotFavourite(holder)
        }
        Picasso.get().load(food.foodImage).error(R.drawable.profile).into(holder.imgFoodImage)

        holder.rlContent.setOnClickListener{
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra("food_id", food.foodId)
            intent.putExtra("foodName", food.foodName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodName)
        val txtFoodPrice: TextView = view.findViewById(R.id.txtFoodPrice)
        val txtFavourite: TextView = view.findViewById(R.id.txtFavourite)
        val txtFoodRating: TextView = view.findViewById(R.id.txtFoodRating)
        val imgFoodImage: ImageView = view.findViewById(R.id.imgFoodImage)
        val rlContent: RelativeLayout = view.findViewById(R.id.rlContent)
    }

    fun isFavourite(holder: HomeViewHolder) {
        holder.txtFavourite.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_favourite_filled,
            0,
            0,
            0
        );
    }

    fun isNotFavourite(holder: HomeViewHolder) {
        holder.txtFavourite.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_favourite_empty,
            0,
            0,
            0
        );
    }
}