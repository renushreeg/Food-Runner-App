package com.internship.foodrunner.model

import org.json.JSONObject

class MenuList {
    var foodArray = arrayListOf<JSONObject>()
    var selectMenuList = arrayListOf<CartItem>()
    var costForOne: Int = 0

    fun addMenuItem(food_item_id: Int, name: String, cost_for_one: Int) {
        var menuItem = JSONObject()
        menuItem.put("food_item_id", food_item_id.toString())
        foodArray.add(menuItem)
        var selectedMenuItem = CartItem(name, cost_for_one.toString())
        selectMenuList.add(selectedMenuItem)
        costForOne += cost_for_one
    }

    fun removeMenuItem(food_item_id: Int, name: String, cost_for_one: Int) {
        var menuItem = JSONObject()
        menuItem.put("food_item_id", food_item_id.toString())
        var index = getIndex(food_item_id)
        if (index != 1100) {
            foodArray.removeAt(index)
            selectMenuList.removeAt(index)
        }
        costForOne -= cost_for_one
    }

    fun getIndex(food_item_id: Int): Int {
        var menuItem = JSONObject()
        menuItem.put("food_item_id", food_item_id.toString())

        foodArray.forEachIndexed { index, it ->
            if (it.toString() == menuItem.toString()) {
                return index
            }
        }
        return 1100
    }

    fun getMenuItem(): ArrayList<JSONObject> {
        return foodArray
    }

    fun getCost(): Int {
        return costForOne
    }

    fun getSelectedMenuList(): ArrayList<CartItem> {
        return selectMenuList
    }

    fun checkInMenuList(food_item_id: Int): Boolean {
        var menuItem = JSONObject()
        menuItem.put("food_item_id", food_item_id.toString())

        foodArray.forEach {
            if (it.toString() == menuItem.toString()) {
                return true
            }
        }
        return false
    }
}