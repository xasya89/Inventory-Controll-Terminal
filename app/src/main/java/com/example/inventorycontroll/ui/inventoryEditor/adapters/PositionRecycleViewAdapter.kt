package com.example.inventorycontroll.ui.inventoryEditor.adapters

import android.content.Context
import android.text.Editable
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
import com.example.inventorycontroll.databinding.InventoryEditorPositionBinding
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel

class PositionRecycleViewAdapter():RecyclerView.Adapter<PositionRecycleViewAdapter.ViewHolder>() {

    private var _positions = mutableListOf<InventoryPositionModel>()
    var onClickGoodName: ((position: InventoryPositionModel) -> Unit)? = null

    fun add(position: InventoryPositionModel){
        _positions.add(0, position)
        notifyItemInserted(0)
    }

    fun addList(positions: List<InventoryPositionModel>){
        _positions.clear()
        _positions.addAll(positions)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = InventoryEditorPositionBinding.bind(view)

        init {
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
                        return true
                    }

                    return false
                }
            })
        }

        fun bind(position: InventoryPositionModel) = with(binding){
            positionGoodName.text = position.goodName
            positionGoodCount.text = Editable.Factory.getInstance().newEditable( position.count.toString())
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