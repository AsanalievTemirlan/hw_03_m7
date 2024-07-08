package com.example.hw_03_m7.ui.medicines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.databinding.ItemMedicinesBinding
import com.example.hw_03_m7.ui.interfaces.OnClickItem

class MedicinesAdapter(private val onLongClickItem: OnClickItem, private val onClickItem: OnClickItem) :
    androidx.recyclerview.widget.ListAdapter<MedicinesModel, MedicinesAdapter.ViewHolder>(
        DiffCallback()
    ) {

    class ViewHolder(private val binding: ItemMedicinesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MedicinesModel) = with(binding) {
            title.text = model.title
            time.text = model.time
            description.text = model.description
            progressBar.progress = model.quantity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMedicinesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnLongClickListener {
            onLongClickItem.onLongClick(getItem(position))
            true
        }
        holder.itemView.setOnClickListener {
            onClickItem.onClick(getItem(position))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MedicinesModel>() {
        override fun areItemsTheSame(oldItem: MedicinesModel, newItem: MedicinesModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MedicinesModel, newItem: MedicinesModel): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
