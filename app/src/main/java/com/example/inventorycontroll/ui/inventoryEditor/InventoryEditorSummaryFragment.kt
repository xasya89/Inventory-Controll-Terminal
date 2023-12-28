package com.example.inventorycontroll.ui.inventoryEditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FragmentInventoryEditorSummaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryEditorSummaryFragment : Fragment() {

    private lateinit var binding: FragmentInventoryEditorSummaryBinding
    private val vm by activityViewModels<InventoryEditorViewModel>()
    private lateinit var listAdapter: ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryEditorSummaryBinding.inflate(inflater)
        listAdapter = ArrayAdapter<String>(
            requireContext(),android.R.layout.simple_list_item_1
        )
        binding.inventorySummaryPositions.adapter = listAdapter
        binding.invnetorySendToServer.setOnClickListener {
            vm.send{
                findNavController().navigate(R.id.nav_inventory_loading)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.inventory.observe(viewLifecycleOwner, {
            binding.inventorySummaryCashMoney.text = it?.startCashMoney.toString()
            binding.inventorySummaryGoodsAll.text = it?.goodsSum.toString()
        })
        vm.groups.observe(viewLifecycleOwner, {
            listAdapter.clear()
            listAdapter.addAll(it.map { it.groupName + "  " + it.sum.toString() })
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = InventoryEditorSummaryFragment()
    }
}