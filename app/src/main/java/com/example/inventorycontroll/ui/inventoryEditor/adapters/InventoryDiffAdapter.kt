package com.example.inventorycontroll.ui.inventoryEditor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.InventoryDiffItemBinding
import com.example.inventorycontroll.ui.inventoryEditor.models.BalanceDiffItemModel

class InventoryDiffAdapter(): RecyclerView.Adapter<InventoryDiffAdapter.ViewHolder>() {

    private var itemes = listOf<BalanceDiffItemModel>()

    fun setItems(_items: List<BalanceDiffItemModel>){
        itemes = _items
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = InventoryDiffItemBinding.bind(view)
        fun bind(item: BalanceDiffItemModel){
            binding.inventoryDiffGoodName.text = item.goodName
            binding.inventoryDiffCountFact.text = item.countFact.toString()
            binding.inventoryDiffCountFromServer.text = item.countFromServer.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_diff_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemes.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemes.get(position))
    }
}