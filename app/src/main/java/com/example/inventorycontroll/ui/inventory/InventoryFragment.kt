package com.example.inventorycontroll.ui.inventory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.inventorycontroll.R
import com.example.inventorycontroll.common.viewModels.KeyListenerViewModel
import com.example.inventorycontroll.databinding.FragmentInventoryEditorBinding

class InventoryFragment : Fragment() {
    private lateinit var binding: FragmentInventoryEditorBinding
    private val keyListenerVm by activityViewModels<KeyListenerViewModel> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryEditorBinding.inflate(inflater)
        keyListenerVm.barcode.observe(viewLifecycleOwner, {

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("has code fragment", keyListenerVm.hashCode().toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InventoryFragment()
    }
}