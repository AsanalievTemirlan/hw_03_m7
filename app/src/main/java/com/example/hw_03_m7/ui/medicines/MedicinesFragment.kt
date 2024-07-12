package com.example.hw_03_m7.ui.medicines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hw_03_m7.R
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.databinding.FragmentMedicinesBinding
import com.example.hw_03_m7.ui.interfaces.OnClickItem
import com.example.hw_03_m7.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicinesFragment : Fragment(), OnClickItem {

    private var _binding: FragmentMedicinesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicinesViewModel by viewModels()
    private lateinit var adapter: MedicinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicinesBinding.inflate(inflater, container, false).apply {
            (activity as? AppCompatActivity)?.let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
                it.supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MedicinesAdapter(onClickItem = this, onLongClickItem = this)
        binding.rvMedicines.adapter = adapter

        viewModel.getAll().observe(viewLifecycleOwner) { medicines ->
            Log.e("TAG", "MFragment $medicines")
            adapter.submitList(medicines)
            medicines.forEach { medicine ->
                val timeInMillis = NotificationUtils.convertTimeToMillis(medicine.time)
                NotificationUtils.scheduleNotification(
                    requireContext(), medicine.title,
                    "Time to take your medication: ${medicine.title}", timeInMillis
                )
            }
        }
    }

    override fun onLongClick(medicinesModel: MedicinesModel) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Medicine")
            setMessage("Are you sure you want to delete this medicine?")
            setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    viewModel.delete(medicinesModel)
                }
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun onClick(medicinesModel: MedicinesModel) {
        val action = MedicinesFragmentDirections.actionNavigationMedicinesToNavigationMake(medicinesModel.id!!)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
