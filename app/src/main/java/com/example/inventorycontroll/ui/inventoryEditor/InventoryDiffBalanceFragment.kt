package com.example.inventorycontroll.ui.inventoryEditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FragmentInventoryDiffBalanceBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class InventoryDiffBalanceFragment() : Fragment() {
    private var inventoryId: Long? = null
    private lateinit var binding: FragmentInventoryDiffBalanceBinding
    private val vm by viewModels<InventoryDiffViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inventoryId = it.getLong(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryDiffBalanceBinding.inflate(inflater)
        binding.inventoryDiffTv.text = inventoryId.toString()
        vm.balance.observe(viewLifecycleOwner,{
            binding.inventoryDiffTv.text = it.count().toString()
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getDiff(inventoryId!!)
    }

    companion object {
        @JvmStatic
        fun newInstance(inventoryId: Long) =
            InventoryDiffBalanceFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, inventoryId)
                }
            }
    }
}