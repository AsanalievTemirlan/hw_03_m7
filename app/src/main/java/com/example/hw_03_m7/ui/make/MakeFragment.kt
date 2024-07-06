package com.example.hw_03_m7.ui.make

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hw_03_m7.R
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.databinding.FragmentMakeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MakeFragment : Fragment() {

    private var _binding: FragmentMakeBinding? = null
    private val binding get() = _binding!!
    private var bottomNavView: BottomNavigationView? = null
    private val viewModel: MakeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavView = activity?.findViewById(R.id.nav_view)
        bottomNavView?.visibility = View.GONE
        setupUI()
        setUpListeners()
    }

    private fun setUpListeners() = with(binding) {
        closeButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_medicines)
        }
        addButton.setOnClickListener {
            val medicineName = etTitle.text.toString()
            val description = etDescription.text.toString()
            val dosageText = dosageTextView.text.toString()
            val time = timeTextView.text.toString()

            if (medicineName.isEmpty() || dosageText.isEmpty() || time.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dosage = dosageText.toIntOrNull() ?: 1
            if (dosage == 0) {
                Toast.makeText(requireContext(), "Некорректная дозировка", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val medicinesModel = MedicinesModel(
                id = 0,
                title = medicineName,
                description = description,
                quantity = dosage,
                time = time
            )
            Toast.makeText(requireContext(), "Медикамент добавлен в карточку", Toast.LENGTH_SHORT)
                .show()

            viewModel.insertMedicines(medicinesModel)
            findNavController().navigate(R.id.navigation_medicines)
        }


    }

    private fun setupUI() {
        binding.selectTimeButton.setOnClickListener {
            showTimePickerDialog()
        }
        binding.selectDosageButton.setOnClickListener {
            showDosagePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                binding.timeTextView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            }, hour, minute, true
        )

        timePickerDialog.show()
    }

    private fun showDosagePickerDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Dosage")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val dosage = input.text.toString()
            if (dosage.isNotEmpty()) {
                binding.dosageTextView.text = "$dosage таблетка/и"
            } else {
                Toast.makeText(requireContext(), "Dosage cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavView?.visibility = View.VISIBLE
        _binding = null
    }
}
