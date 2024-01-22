package com.example.inventorycontroll.ui.inventoryEditor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FindGoodItemBinding
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.ui.inventoryEditor.models.FindGoodModel

class FindGoodAdapter() :RecyclerView.Adapter<FindGoodAdapter.ViewHolder>() {

    private var goods = listOf<FindGoodModel>()
    var onChangeSelect: ((good: FindGoodModel) -> Unit )? = null
    fun setGoods(list: List<FindGoodModel>){
        goods = list
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = FindGoodItemBinding.bind(view)
        init {
            binding.findGoodBtnSelect.setOnClickListener{
                val good = goods.get(adapterPosition)
                good.isSelected=!good.isSelected
                notifyItemChanged(adapterPosition)
                onChangeSelect?.invoke(good)
            }
        }

        fun bind(item: FindGoodModel)= with(binding){
            findGoodName.text = item.name
            findGoodPrice.text = item.price.toString() + "  кол-во: " + item.balance.toString()
            findGoodBtnSelect.setImageResource(if(item.isSelected) R.drawable.baseline_check_box_24 else R.drawable.baseline_check_box_outline_blank_24)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.find_good_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goods.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goods.get(position))
    }
}