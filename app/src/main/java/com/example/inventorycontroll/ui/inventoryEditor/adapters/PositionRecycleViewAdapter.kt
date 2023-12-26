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
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorycontroll.R
import com.example.inventorycontroll.common.Debounce
import com.example.inventorycontroll.databinding.InventoryEditorPositionBinding
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class PositionRecycleViewAdapter(private val onChangeCount: (Long, BigDecimal)->Unit):RecyclerView.Adapter<PositionRecycleViewAdapter.ViewHolder>() {

    private var _positions = mutableListOf<InventoryPositionModel>()

    fun add(position: InventoryPositionModel){
        _positions.add(0, position)
        notifyItemInserted(0)
    }

    fun addList(positions: List<InventoryPositionModel>){
        _positions.clear()
        _positions.addAll(positions)
        notifyDataSetChanged()
    }

    //var onChangeCount: ((goodId: Long, count: BigDecimal) -> Unit)? = null

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = InventoryEditorPositionBinding.bind(view)

        private fun onChange() = with(binding) {
            val position = _positions.get(adapterPosition)
            val count: BigDecimal =
                if (positionGoodCount.text.toString() == "") BigDecimal(0) else BigDecimal(
                    positionGoodCount.text.toString()
                )
            onChangeCount?.invoke(position.goodId, count)
        }
        private var isBindComplite = false
        init {
            binding.positionGoodCount.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(isBindComplite) onChange()
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
            positionGoodCount.text = Editable.Factory.getInstance().newEditable( position.count.toString())
            isBindComplite = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.inventory_editor_position, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _positions.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_positions.get(position))
    }
}