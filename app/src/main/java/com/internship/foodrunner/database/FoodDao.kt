package com.internship.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    fun insertFood(bookEntity: FoodEntity)

    @Delete
    fun deleteFood(bookEntity: FoodEntity)

    @Query("SELECT * FROM food")
    fun getAllFoods(): List<FoodEntity>

    @Query("SELECT * FROM food WHERE id = :foodId")
    fun getFoodById(foodId: String): FoodEntity
}