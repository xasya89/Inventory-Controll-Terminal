package com.example.inventorycontroll.ui.inventoryEditor

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventorycontroll.R
import com.example.inventorycontroll.common.Debounce
import com.example.inventorycontroll.databinding.FragmentFindGoodBinding
import com.example.inventorycontroll.ui.inventoryEditor.adapters.FindGoodAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FindGoodFragment : Fragment() {

    private lateinit var binding: FragmentFindGoodBinding
    private val vm by activityViewModels<InventoryEditorViewModel>()
    private val findGoodViewModel by viewModels<FindGoodViewModel>()
    private lateinit var adapter: FindGoodAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindGoodBinding.inflate(inflater)
        binding.findGoodRecycleView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FindGoodAdapter()
        binding.findGoodRecycleView.adapter = adapter

        val debounce = Debounce(300L, {
            findGoodViewModel.find(it)
        })

        binding.findGoodEdit.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                debounce.offer(p0)
                return true
            }

        })

        binding.findGoodSave.setOnClickListener {
            vm.addPositions(findGoodViewModel.goods.value!!.filter { it.isSelected })
            findNavController().navigate(R.id.nav_inventory_editor_main_viewpage)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findGoodViewModel.goods.observe(viewLifecycleOwner,{
            adapter.setGoods(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FindGoodFragment().apply { }
    }
}