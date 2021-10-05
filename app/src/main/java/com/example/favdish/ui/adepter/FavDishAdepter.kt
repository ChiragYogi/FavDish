package com.example.favdish.ui.adepter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdish.databinding.ItemDishLayoutBinding
import com.example.favdish.models.FavDish

class FavDishAdepter(private val listener: OnItemClick):  ListAdapter<FavDish, FavDishAdepter.MyViewHolder>(
    DiffUtilCallBack()
){
    class DiffUtilCallBack: DiffUtil.ItemCallback<FavDish>() {

        override fun areItemsTheSame(oldItem: FavDish, newItem: FavDish): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavDish, newItem: FavDish): Boolean {
            return oldItem == newItem
        }

    }

   inner class MyViewHolder(private val binding: ItemDishLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavDish) {
            binding.apply {
                Glide.with(root.context).load(item.image).into(ivDishImage)
                tvDishTitle.text = item.title
            }
        }

        init {
           binding.root.setOnClickListener {
                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION){
                    val dish = getItem(position)
                    listener.onDishItemClick(dish)
                }
            }

            binding.ibEditDish.setOnClickListener {
                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION){
                    val dish = getItem(position)
                    listener.onDishEditClick(dish)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDishLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnItemClick{
        fun onDishItemClick(favDish: FavDish)
        fun onDishEditClick(favDish: FavDish)
    }
}