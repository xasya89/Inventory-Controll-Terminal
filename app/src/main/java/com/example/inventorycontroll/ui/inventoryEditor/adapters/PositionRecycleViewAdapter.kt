package com.example.inventorycontroll.ui.inventoryEditor.adapters

import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnKeyListener
import android.view.ViewGroup
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

            binding.positionGoodCount.setOnKeyListener(object : OnKeyListener{
                override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                    if(p2?.action == KeyEvent.ACTION_DOWN && p2?.keyCode==KeyEvent.KEYCODE_ENTER){
                        p0?.clearFocus()
                    }
                    return true
                }
            })
        }

        fun bind(position: InventoryPositionModel) = with(binding){
            positionGoodName.text = position.goodName

            /*
            positionGoodName.setOnClickListener(object: OnClickListener{
                override fun onClick(p0: View?) {
                    onClickGoodName?.invoke(position)
                }

            })

             */
            //positionGoodCount.text = "" + position.count.toString()
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