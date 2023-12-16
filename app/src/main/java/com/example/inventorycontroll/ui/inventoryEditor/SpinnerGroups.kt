package com.example.inventorycontroll.ui.inventoryEditor

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup


class SpinnerGroups(val context: Context, val spinner: Spinner, onSelectedChange: (group: InventoryGroup) -> Unit) {
    private val groups = mutableListOf<InventoryGroup>()
    private val groupNames = mutableListOf<String>()
    val adapter = ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, groupNames)
    fun setGroups(_groups: List<InventoryGroup>){
        groups.clear()
        groups.addAll(_groups)
        groupNames.clear()
        groupNames.addAll(groups.map { it.groupName })
        adapter.notifyDataSetChanged()
    }
    init {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                onSelectedChange.invoke(groups.get(position))
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
    }
}