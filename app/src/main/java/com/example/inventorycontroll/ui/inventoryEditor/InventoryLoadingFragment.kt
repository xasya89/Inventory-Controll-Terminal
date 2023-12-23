package com.example.inventorycontroll.ui.inventoryEditor

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FragmentInventoryLoadingBinding
import com.example.inventorycontroll.databinding.InputTextDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryLoadingFragment : Fragment() {
    private lateinit var binding: FragmentInventoryLoadingBinding
    private val vm by activityViewModels<InventoryEditorViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryLoadingBinding.inflate(inflater)
        binding.inventoryLoaderCreateBtn.setOnClickListener {
            showDialog({
                vm.createIncentory(it.toBigDecimal(),{
                    binding.inventoryLoaderOpenBtn.setOnClickListener {
                        findNavController().navigate(R.id.nav_inventory_editor)
                    }
                })
            })
        }
        binding.inventoryLoaderOpenBtn.setOnClickListener {
            findNavController().navigate(R.id.nav_inventory_editor)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getInventory()
        vm.inventory.observe(viewLifecycleOwner, {
            binding.inventoryLoaderOpenBtn.visibility = if(it==null) View.GONE else View.VISIBLE
        })
    }

    private fun showDialog(callBackOk:((String) -> Unit)? = null){
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogBinding = InputTextDialogBinding.inflate(inflater)
        dialogBinding.inputDialogCount.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL

        dialogBinding.inputDialogCount.isFocusableInTouchMode=true
        dialogBinding.inputDialogCount.requestFocus()
        with(builder){
            dialogBinding.inputDialogCount.requestFocus()
            dialogBinding.textView2.text = "Денег в кассе"
            setView(dialogBinding.root)
        }
        val alertDialog = builder.create()
        dialogBinding.inputDialogSuccessBtn.setOnClickListener {
            if(dialogBinding.inputDialogCount.text.toString()=="") return@setOnClickListener

            alertDialog.dismiss()
            callBackOk?.invoke(dialogBinding.inputDialogCount.text.toString())
        }
        dialogBinding.inputDialogCancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
        dialogBinding.inputDialogCount.requestFocus()
        dialogBinding.inputDialogCount.setSingleLine()
        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InventoryLoadingFragment()
    }
}