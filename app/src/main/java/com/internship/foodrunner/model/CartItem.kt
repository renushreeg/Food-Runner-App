package com.internship.foodrunner.model

import org.json.JSONObject

class CartItem(val cartMenuItemName: String, val cartMenuItemCost: String) {
    companion object {
        fun convertToJsonObject(cartItem: CartItem): JSONObject {
            val json = JSONObject()
            json.put("cartMenuItemName", cartItem.cartMenuItemName)
            json.put("cartMenuItemCost", cartItem.cartMenuItemCost)
            return json
        }

        fun convertFromJsonObject(json: JSONObject): CartItem {
            return CartItem(json.getString("cartMenuItemName"), json.getString("cartMenuItemCost"))
        }
    }

    override fun toString(): String {
        return "CartItem(cartMenuItemName='$cartMenuItemName', cartMenuItemCost='$cartMenuItemCost')"
    }
}