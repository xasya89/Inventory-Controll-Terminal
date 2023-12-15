package com.example.inventorycontroll.ui.inventoryEditor

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.inventorycontroll.R
import com.example.inventorycontroll.common.viewModels.KeyListenerViewModel
import com.example.inventorycontroll.databinding.FragmentInventoryEditorBinding
import com.example.inventorycontroll.databinding.InputTextDialogBinding
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import kotlinx.coroutines.selects.select
import java.math.BigDecimal

class InventoryEditorFragment : Fragment() {

    private lateinit var binding: FragmentInventoryEditorBinding
    private val keyListenerVm by activityViewModels<KeyListenerViewModel> ()
    private val vm by viewModels<InventoryEditorViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryEditorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyListenerVm.findGood.observe(viewLifecycleOwner, {good->

            if(good!=null)
                showDialog(good)
        })
    }

    private fun showDialog(good: Good){
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogBinding = InputTextDialogBinding.inflate(inflater)

        dialogBinding.inputDialogCount.isFocusableInTouchMode=true
        dialogBinding.inputDialogCount.requestFocus()
        with(builder){
            dialogBinding.inputDialogCount.requestFocus()
            setTitle(good.name)
            setView(dialogBinding.root)
        }
        val alertDialog = builder.create()
        dialogBinding.inputDialogSuccessBtn.setOnClickListener {
            if(dialogBinding.inputDialogCount.text.toString()=="") return@setOnClickListener
            val count = BigDecimal(dialogBinding.inputDialogCount.text.toString())
            vm.addPosition(good, count)
            alertDialog.dismiss()
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
            InventoryEditorFragment()
    }
}