package com.example.inventorycontroll.ui.inventoryEditor.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.inventorycontroll.ui.inventoryEditor.InventoryEditorFragment
import com.example.inventorycontroll.ui.inventoryEditor.InventoryEditorSummaryFragment

class InventoryViewPageAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = if(position==0) InventoryEditorFragment() else InventoryEditorSummaryFragment()
}