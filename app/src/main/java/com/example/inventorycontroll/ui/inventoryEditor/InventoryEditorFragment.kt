package com.example.inventorycontroll.ui.inventoryEditor

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FragmentInventoryEditorBinding
import com.example.inventorycontroll.databinding.InputTextDialogBinding
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.ui.inventoryEditor.adapters.PositionRecycleViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal


@AndroidEntryPoint
class InventoryEditorFragment : Fragment() {

    private lateinit var binding: FragmentInventoryEditorBinding
    private val vm by activityViewModels<InventoryEditorViewModel>()
    private val rcAdapter=PositionRecycleViewAdapter({goodId, newCount -> vm.changeCountInPosition(goodId, newCount)})

    private lateinit var spinnerGroups: SpinnerGroups

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryEditorBinding.inflate(inflater)
        with(binding){
            inventoryEditorRc.layoutManager = LinearLayoutManager(context)
            inventoryEditorRc.adapter= rcAdapter
            inventoryEditorSearchBtn.setOnClickListener {
                findNavController().navigate(R.id.findGoodFragment)
            }

            spinnerGroups = SpinnerGroups(requireContext(), inventoryEditorGroupSelectList, {
                vm.changeSelectGroup(it)
            })

            binding.inventoryEditorAddGroupBtn.setOnClickListener {
                showDialog("Название группы","",false, {
                    vm.addGroup(it)
                })
            }

            binding.inventoryEditorPositionsSave.setOnClickListener {
                vm.savePositions()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.groups.observe(viewLifecycleOwner, {
            if(it!=null)
                spinnerGroups.setGroups(it)
        })
        vm.selectGroup.observe(viewLifecycleOwner,{
            if(it==null){
                binding.inventoryEditorGroupSelectList.setSelection(-1)
                return@observe
            }
            val pos = vm.groups.value!!.indexOf(it)
            binding.inventoryEditorGroupSelectList.setSelection(pos)
        })

        vm.positions.observe(viewLifecycleOwner, {
            rcAdapter.setPositions(it)
        })

        vm.isSaveState.observe(viewLifecycleOwner,{
            binding.inventoryEditorPositionsSave.visibility = if(it==true) View.VISIBLE else View.GONE
        })

        initClipboardService()
    }

    private fun initClipboardService(){
        val cm = requireContext().getSystemService<ClipboardManager>()
        cm!!.addPrimaryClipChangedListener {
            val clip = cm.primaryClip!!.getItemAt(0)
            val barcode = clip.text.toString()
            if(barcode=="") return@addPrimaryClipChangedListener
            vm.getGood(barcode, {good->
                val position = vm.positions.value?.find { it.goodId==good.id }
                if(position==null)
                    showDialog(good.name, "",true, {
                        if(it=="") return@showDialog
                        val count = BigDecimal(it)
                        vm.addPosition(good, count)
                    })
                else
                    showDialog(good.name, position.count.toString(),true, {
                        if(it=="") return@showDialog
                        val count = BigDecimal(it)
                        vm.addPosition(good, count)
                    })
            })
        }
    }


    private fun showDialog(title:String, prevValue: String, isNumericInputType: Boolean = false, callBackOk:((text: String) -> Unit)? = null, callBackCancel: (()->Unit)? = null){
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogBinding = InputTextDialogBinding.inflate(inflater)
        if(isNumericInputType)
            dialogBinding.inputDialogCount.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL

        dialogBinding.inputDialogCount.isFocusableInTouchMode=true
        dialogBinding.inputDialogCount.requestFocus()
        dialogBinding.inputDialogCount.text = Editable.Factory().newEditable(prevValue)
        with(builder){
            dialogBinding.inputDialogCount.requestFocus()
            dialogBinding.textView2.text = title
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