package com.example.hw_03_m7.ui.make

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    private val viewModel: MakeViewModel by viewModels()
    private val args: MakeFragmentArgs by navArgs()
    private var existingMedicine: MedicinesModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        setupUI()
        initializeData()
        setUpListeners()
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        activity?.findViewById<Button>(R.id.btn_plus)?.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
        activity?.findViewById<Button>(R.id.btn_plus)?.visibility = View.VISIBLE
    }

    private fun setupUI() = with(binding) {
        selectTimeButton.setOnClickListener { showTimePickerDialog() }
        selectDateButton.setOnClickListener { showDatePickerDialog() }
        selectDosageButton.setOnClickListener { showDosagePickerDialog() }
        timeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            dateLayout.isVisible = isChecked
            delayedMedicineButton.isEnabled = isChecked
            if (!isChecked) dateTextView.text = null
        }
    }

    private fun initializeData() {
        val medicineId = args.medicinesId
        if (medicineId != -1) {
            viewModel.getMedicineId(medicineId).observe(viewLifecycleOwner) { medicine ->
                medicine?.let {
                    existingMedicine = it
                    bindMedicineData(it)
                }
            }
        }
    }

    private fun bindMedicineData(medicine: MedicinesModel) = with(binding) {
        etTitle.setText(medicine.title)
        etDescription.setText(medicine.description)
        dosageTextView.text = medicine.quantity.toString()
        timeTextView.text = medicine.time
        dateTextView.text = medicine.date
    }

    private fun setUpListeners() = with(binding) {
        closeButton.setOnClickListener { navigateBack() }
        addButton.setOnClickListener { handleMedicineAction(isUpdate = false) }
        changeButton.setOnClickListener { handleMedicineAction(isUpdate = true) }
        delayedMedicineButton.setOnClickListener {  }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.navigation_medicines)
    }

    private fun handleMedicineAction(isUpdate: Boolean) = with(binding) {
        val medicineName = etTitle.text.toString()
        val description = etDescription.text.toString()
        val dosageText = dosageTextView.text.toString()
        val time = timeTextView.text.toString()
        val date = dateTextView.text.toString()

        if (medicineName.isEmpty() || description.isEmpty() || dosageText.isEmpty() || time.isEmpty() || date.isEmpty()) {
            showToast("Заполните все поля")
            return
        }

        val dosage = dosageText.toIntOrNull() ?: 1
        if (dosage == 0) {
            showToast("Некорректная дозировка")
            return
        }

        val medicinesModel = existingMedicine?.copy(
            title = medicineName, description = description, quantity = dosage, time = time, date = date
        ) ?: MedicinesModel(
            title = medicineName, description = description, quantity = dosage, time = time, date = date
        )

        if (isUpdate) {
            viewModel.updateMedicine(medicinesModel)
            showToast("Медикамент обновлен")
        } else {
            viewModel.insertMedicines(medicinesModel)
            showToast("Медикамент добавлен")
        }
        navigateBack()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            binding.timeTextView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            binding.dateTextView.text = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
        }, year, month, day).show()
    }

    private fun showDosagePickerDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Dosage")
            val input = EditText(requireContext()).apply { inputType = InputType.TYPE_CLASS_NUMBER }
            setView(input)
            setPositiveButton("OK") { _, _ ->
                val dosage = input.text.toString()
                if (dosage.isNotEmpty()) {
                    binding.dosageTextView.text = dosage
                } else {
                    showToast("Dosage cannot be empty")
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
        _binding = null
    }
}
