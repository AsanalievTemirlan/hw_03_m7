package com.example.hw_03_m7.ui.medicines

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.databinding.ItemMedicinesBinding
import com.example.hw_03_m7.ui.interfaces.OnClickItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MedicinesAdapter(
    private val onLongClickItem: OnClickItem,
    private val onClickItem: OnClickItem
) : androidx.recyclerview.widget.ListAdapter<MedicinesModel, MedicinesAdapter.ViewHolder>(
    DiffCallback()
) {

    class ViewHolder(private val binding: ItemMedicinesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MedicinesModel) = with(binding) {
            title.text = model.title
            description.text = model.description
            progressBar.progress = model.quantity
            time.text =
                if (model.date != "No date selected") model.date + " " + model.time else model.time

            val currentDate = Calendar.getInstance().time
            val delayedDate = if (model.date != null && model.date != "No date selected")
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(model.date)
            else
                null

            if (delayedDate != null && currentDate.before(delayedDate)) (root as CardView).setCardBackgroundColor(Color.GRAY)

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
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MedicinesModel, newItem: MedicinesModel): Boolean {
            return oldItem == newItem
        }
    }
}
