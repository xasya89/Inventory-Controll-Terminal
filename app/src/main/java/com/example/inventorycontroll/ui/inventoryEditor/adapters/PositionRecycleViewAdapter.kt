package com.example.inventorycontroll.ui.inventoryEditor.adapters

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.InventoryEditorPositionBinding
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel

class PositionRecycleViewAdapter():RecyclerView.Adapter<PositionRecycleViewAdapter.ViewHolder>() {

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

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = InventoryEditorPositionBinding.bind(view)

        fun bind(position: InventoryPositionModel) = with(binding){
            positionGoodName.text = position.goodName
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