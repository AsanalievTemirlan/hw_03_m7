package com.example.hw_03_m7.ui.medicines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hw_03_m7.R
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.databinding.FragmentMedicinesBinding
import com.example.hw_03_m7.ui.interfaces.OnClickItem
import com.example.hw_03_m7.ui.make.MakeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicinesFragment : Fragment(), OnClickItem {

    private var _binding: FragmentMedicinesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MedicinesAdapter
    private val viewModel: MedicinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicinesBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MedicinesAdapter(onClickItem = this, onLongClickItem = this)
        addData()
        binding.rvMedicines.adapter = adapter
        initListener()

    }



    private fun addData() {
        viewModel.getAll().observe(viewLifecycleOwner) { medicines ->
            Log.e("TAG", "MFragment $medicines")
            adapter.submitList(medicines)
        }
    }


    private fun initListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLongClick(medicinesModel: MedicinesModel) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder){
            setTitle("Delete Medicine")
            setMessage("Are you sure you want to delete this medicine?")
            setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    viewModel.delete(medicinesModel)
                }
            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun onClick(medicinesModel: MedicinesModel) {
        val action = MedicinesFragmentDirections.actionNavigationMedicinesToNavigationMake(medicinesModel.id!!)
        findNavController().navigate(action)
    }
}
