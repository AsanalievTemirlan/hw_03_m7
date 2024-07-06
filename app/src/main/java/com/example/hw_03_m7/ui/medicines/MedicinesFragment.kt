package com.example.hw_03_m7.ui.medicines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hw_03_m7.R
import com.example.hw_03_m7.databinding.FragmentMedicinesBinding
import com.example.hw_03_m7.ui.make.MakeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicinesFragment : Fragment() {

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
        adapter = MedicinesAdapter()
        binding.rvMedicines.adapter = adapter
        initListener()
        addData()
    }

    private fun addData() {
        viewModel.getAll().observe(viewLifecycleOwner) { medicines ->
            adapter.submitList(medicines)
        }
    }

    private fun initListener() {
        binding.btnPlus.setOnClickListener {
            findNavController().navigate(R.id.navigation_make)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
