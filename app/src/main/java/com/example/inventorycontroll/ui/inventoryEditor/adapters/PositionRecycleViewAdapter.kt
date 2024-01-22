package com.example.inventorycontroll.ui.inventoryEditor.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorycontroll.R
import com.example.inventorycontroll.common.Debounce
import com.example.inventorycontroll.common.DebounceAction
import com.example.inventorycontroll.databinding.InventoryEditorPositionBinding
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID

class PositionDiffUtillCallback(
    private val oldPositions: List<InventoryPositionModel>,
    private val newPositions: List<InventoryPositionModel>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int =oldPositions.size

    override fun getNewListSize(): Int = newPositions.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldPositions.get(oldItemPosition)
        val new = newPositions.get(newItemPosition)
        return old.goodId==new.goodId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldPositions.get(oldItemPosition)
        val new = newPositions.get(newItemPosition)
        return old.count==new.count
    }

}

class PositionRecycleViewAdapter(private val onChangeCount: (UUID, Long, BigDecimal)->Unit):RecyclerView.Adapter<PositionRecycleViewAdapter.ViewHolder>() {

    private var positions = listOf<InventoryPositionModel>()

    fun setPositions(items: List<InventoryPositionModel>){
        val diffCalback = PositionDiffUtillCallback(positions, items)
        val diifResult = DiffUtil.calculateDiff(diffCalback)
        positions = items
        diifResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = InventoryEditorPositionBinding.bind(view)

        private fun onChange() = with(binding) {
            val position = positions.get(adapterPosition)
            val count: BigDecimal =
                if (positionGoodCount.text.toString() == "") BigDecimal(0) else BigDecimal(
                    positionGoodCount.text.toString()
                )
            onChangeCount?.invoke(position.uuid!!, position.goodId, count)
        }

        private fun hideKeyBoard() = with(binding){
            positionGoodCount?.clearFocus()
            val inputMethodManager = positionGoodCount?.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(positionGoodCount.windowToken, 0)
        }

        private var isBindComplite = false
        private val debounce = DebounceAction(1000L,{
            onChange()
        })
        init {
            binding.positionGoodCount.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(isBindComplite)
                        debounce.offer()
                }
                override fun afterTextChanged(p0: Editable?) { }
            })

            binding.positionGoodCount.setOnEditorActionListener(object: OnEditorActionListener{
                override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event?.getAction() == KeyEvent.ACTION_DOWN
                        && event?.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                    {
                        hideKeyBoard()
                        view?.clearFocus()
                        val inputMethodManager = view?.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                        onChange()
                        return true
                    }

                    return false
                }
            })
        }

        fun bind(position: InventoryPositionModel) = with(binding){
            isBindComplite = false
            positionGoodName.text = position.goodName
            positionGoodCount.text = Editable.Factory.getInstance().newEditable( if(position.count==BigDecimal(0)) "" else position.count.toString())
            isBindComplite = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.inventory_editor_position, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return positions.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(positions.get(position))
    }
}