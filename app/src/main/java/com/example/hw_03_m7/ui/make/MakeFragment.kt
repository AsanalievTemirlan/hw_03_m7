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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavView?.visibility = View.GONE
        setupUI()
        setUpListeners()
        initialization()
        update()
        checkBoxListener()
    }

    private fun checkBoxListener() = with(binding) {
        timeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dateLayout.visibility = View.VISIBLE
                delayedMedicineButton.isEnabled = true
            } else {
                dateLayout.visibility = View.GONE
                delayedMedicineButton.isEnabled = false
                dateTextView.text = null
            }
        }
    }

    private fun update() = with(binding) {
        val medicineId = args.medicinesId
        if (medicineId != -1) {
            viewModel.getMedicineId(medicineId).observe(viewLifecycleOwner) { medicine ->
                medicine?.let {
                    existingMedicine = it
                    etTitle.setText(it.title)
                    etDescription.setText(it.description)
                    dosageTextView.text = it.quantity.toString()
                    timeTextView.text = it.time
                    dateTextView.text = it.date
                }
            }
        }
    }

    private fun handleMedicineAction(isUpdate: Boolean) = with(binding) {
        val medicineName = etTitle.text.toString()
        val description = etDescription.text.toString()
        val dosageText = dosageTextView.text.toString()
        val time = timeTextView.text.toString()
        val date = dateTextView.text.toString()

        if (medicineName.isEmpty() || dosageText.isEmpty() || time.isEmpty() || date.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val dosage = dosageText.toIntOrNull() ?: 1
        if (dosage == 0) {
            Toast.makeText(requireContext(), "Некорректная дозировка", Toast.LENGTH_SHORT).show()
            return
        }

        val medicinesModel = existingMedicine?.copy(
            title = medicineName,
            description = description,
            quantity = dosage,
            time = time,
            date = date
        ) ?: MedicinesModel(
            title = medicineName,
            description = description,
            quantity = dosage,
            time = time,
            date = date
        )

        if (isUpdate) {
            viewModel.updateMedicine(medicinesModel)
            Toast.makeText(requireContext(), "Медикамент обновлен", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertMedicines(medicinesModel)
            Toast.makeText(requireContext(), "Медикамент добавлен", Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.navigation_medicines)
    }


    private fun setUpListeners() = with(binding) {
        closeButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_medicines)
        }
        addButton.setOnClickListener {
            handleMedicineAction(false)
        }
        changeButton.setOnClickListener {
            handleMedicineAction(true)
        }
        delayedMedicineButton.setOnClickListener {

        }
    }

    private fun setupUI() = with(binding) {
        selectTimeButton.setOnClickListener {
            showTimePickerDialog()
        }
        selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }
        selectDosageButton.setOnClickListener {
            showDosagePickerDialog()
        }
        delayedMedicineButton.setOnClickListener {

        }
    }

    private fun showTimePickerDialog() = with(binding) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                timeTextView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun showDatePickerDialog() = with(binding) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                dateTextView.text =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun showDosagePickerDialog() = with(binding) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Dosage")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val dosage = input.text.toString()
            if (dosage.isNotEmpty()) {
                dosageTextView.text = dosage
            } else {
                Toast.makeText(requireContext(), "Dosage cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun initialization() {
        val btnPlus = activity?.findViewById<Button>(R.id.btn_plus)
        btnPlus?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavView?.visibility = View.VISIBLE
        val btnPlus = activity?.findViewById<Button>(R.id.btn_plus)
        btnPlus?.visibility = View.VISIBLE
        _binding = null
    }
}
