package com.example.hw_03_m7.ui.make

import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    private var btnPlus: Button? = null
    private var medicineId = -1
    private var existingMedicine: MedicinesModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavView = activity?.findViewById(R.id.nav_view)
        bottomNavView?.visibility = View.GONE
        setupUI()
        setUpListeners()
        initialization()
        update()
    }

    private fun update() = with(binding) {
        arguments?.let {
            medicineId = it.getInt("medicineId", -1)
        }
        if (medicineId != -1) {
            viewModel.getMedicineId(medicineId).observe(viewLifecycleOwner) { medicine ->
                Log.e("TAG", "Update $medicine")
                medicine?.let {
                    existingMedicine = it
                    etTitle.setText(it.title)
                    etDescription.setText(it.description)
                    dosageTextView.text = it.quantity.toString()
                    timeTextView.text = it.time
                }
            }
        }
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

            val medicinesModel = existingMedicine?.copy(
                title = medicineName,
                description = description,
                quantity = dosage,
                time = time
            ) ?: MedicinesModel(
                title = medicineName,
                description = description,
                quantity = dosage,
                time = time
            )

            if (existingMedicine != null) {
                viewModel.updateMedicine(medicinesModel)
                Toast.makeText(requireContext(), "Медикамент обновлен", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.insertMedicines(medicinesModel)
                Toast.makeText(requireContext(), "Медикамент добавлен", Toast.LENGTH_SHORT)
                    .show()
            }

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
                binding.dosageTextView.text = dosage
            } else {
                Toast.makeText(requireContext(), "Dosage cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun initialization() {
        btnPlus = activity?.findViewById(R.id.btn_plus)
        btnPlus?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavView?.visibility = View.VISIBLE
        btnPlus?.visibility = View.VISIBLE
        _binding = null
    }
}
