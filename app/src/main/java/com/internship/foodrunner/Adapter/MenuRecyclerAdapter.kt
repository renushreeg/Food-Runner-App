package com.internship.foodrunner.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.internship.foodrunner.R
import com.internship.foodrunner.model.MenuItem
import com.internship.foodrunner.model.MenuList
import org.json.JSONArray

class MenuRecyclerAdapter(
    val context: Context,
    val itemList: ArrayList<MenuItem>,
    val menuItemList: MenuList
) :
    RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtMenuItemCount: TextView = view.findViewById(R.id.txtMenuItemCount)
        val textMenuItemName: TextView = view.findViewById(R.id.textMenuItemName)
        val textMenuItemCost: TextView = view.findViewById(R.id.textMenuItemCost)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_details_single_row, parent, false)
        return MenuRecyclerAdapter.MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = itemList[position]
        holder.txtMenuItemCount.text = (position + 1).toString()
        holder.textMenuItemName.text = menu.name
        holder.textMenuItemCost.text = "Rs. " + menu.costForOne

        holder.btnAdd.setOnClickListener {
            if (!menuItemList.checkInMenuList((menu.id).toInt())) {
                menuItemList.addMenuItem((menu.id).toInt(), menu.name, (menu.costForOne).toInt())
                holder.btnAdd.setText("Remove")
                val noFavColor = ContextCompat.getColor(context, R.color.yellow)
                holder.btnAdd.setBackgroundColor(noFavColor)
            } else {
                menuItemList.removeMenuItem((menu.id).toInt(), menu.name, (menu.costForOne).toInt())
                holder.btnAdd.setText("Add")
                val noFavColor = ContextCompat.getColor(context, R.color.tool_bar)
                holder.btnAdd.setBackgroundColor(noFavColor)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}