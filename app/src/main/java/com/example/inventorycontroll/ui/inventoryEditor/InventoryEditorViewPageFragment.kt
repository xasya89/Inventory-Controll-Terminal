package com.example.inventorycontroll.ui.inventoryEditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FragmentInventoryEditorViewPageBinding
import com.example.inventorycontroll.ui.inventoryEditor.adapters.InventoryViewPageAdapter

class InventoryEditorViewPageFragment : Fragment() {

    private lateinit var binding: FragmentInventoryEditorViewPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryEditorViewPageBinding.inflate(inflater)
        binding.inventoryEditorMainViewpageContainer.adapter = InventoryViewPageAdapter(this)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InventoryEditorViewPageFragment()
    }
}