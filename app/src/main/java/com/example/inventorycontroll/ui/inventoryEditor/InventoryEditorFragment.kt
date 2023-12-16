package com.example.inventorycontroll.ui.inventoryEditor

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycontroll.R
import com.example.inventorycontroll.common.viewModels.KeyListenerViewModel
import com.example.inventorycontroll.databinding.FragmentInventoryEditorBinding
import com.example.inventorycontroll.databinding.InputTextDialogBinding
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.math.BigDecimal

@AndroidEntryPoint
class InventoryEditorFragment : Fragment() {

    private lateinit var binding: FragmentInventoryEditorBinding
    private val keyListenerVm by activityViewModels<KeyListenerViewModel> ()
    private val vm by viewModels<InventoryEditorViewModel>()

    private lateinit var spinnerGroups: SpinnerGroups
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
                showDialog(good.name, true, {
                    if(it=="") return@showDialog
                    val count = BigDecimal(it)
                    vm.addPosition(good, count)
                })
        })
        spinnerGroups = SpinnerGroups(requireContext(), binding.inventoryEditorGroupSelectList, {})

        init()
        binding.inventoryEditorAddGroupBtn.setOnClickListener {
            showDialog("Название группы",false, {
                vm.addGroup(it, {
                    spinnerGroups.setGroups(vm.getGroups())
                })
            })
        }
    }

    private fun  init(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            if(vm.getExistInventory()!=null) {
                viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
                    spinnerGroups.setGroups(vm.getGroups())
                }
                return@launch
            }
            viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
                showDialog("В кассе", true, {
                    val count = BigDecimal(it)
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        vm.createInventory(count)
                    }
                })
            }
        }
    }

    private fun showDialog(title:String, isNumericInputType: Boolean = false, callBackOk:((text: String) -> Unit)? = null, callBackCancel: (()->Unit)? = null){
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogBinding = InputTextDialogBinding.inflate(inflater)
        if(isNumericInputType)
            dialogBinding.inputDialogCount.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL

        dialogBinding.inputDialogCount.isFocusableInTouchMode=true
        dialogBinding.inputDialogCount.requestFocus()
        with(builder){
            dialogBinding.inputDialogCount.requestFocus()
            setTitle(title)
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
            callBackCancel?.invoke()
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