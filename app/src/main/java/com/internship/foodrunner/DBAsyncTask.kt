package com.internship.foodrunner

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.internship.foodrunner.database.FoodDatabase
import com.internship.foodrunner.database.FoodEntity

class DBAsyncTask(val context: Context, val foodEntity: FoodEntity, val mode: Int) :
    AsyncTask<Void, Void, Boolean>() {
    val db = Room.databaseBuilder(context, FoodDatabase::class.java, "food-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                val book: FoodEntity? = db.foodDao().getFoodById(foodEntity.id.toString())
                db.close()
                return book != null
            }
            2 -> {
                db.foodDao().insertFood(foodEntity)
                db.close()
                return true
            }
            3 -> {
                db.foodDao().deleteFood(foodEntity)
                db.close()
                return true
            }
        }
        return false
    }
}