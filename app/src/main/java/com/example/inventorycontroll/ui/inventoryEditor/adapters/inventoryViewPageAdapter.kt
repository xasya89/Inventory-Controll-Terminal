package com.example.inventorycontroll.ui.inventoryEditor.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.inventorycontroll.ui.inventoryEditor.InventoryDiffBalanceFragment
import com.example.inventorycontroll.ui.inventoryEditor.InventoryEditorFragment
import com.example.inventorycontroll.ui.inventoryEditor.InventoryEditorSummaryFragment

class InventoryViewPageAdapter(private val inventoryId: Long, private val fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position){
        0 -> InventoryEditorFragment()
        1 -> InventoryDiffBalanceFragment.newInstance(inventoryId)
        2 -> InventoryEditorSummaryFragment()
        else -> InventoryEditorFragment()
    }
}