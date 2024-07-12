package com.example.hw_03_m7.ui.medicines

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.example.hw_03_m7.ui.notification.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

            medicines.forEach { medicine ->
                val timeInMillis = convertTimeToMillis(medicine.time)
                scheduleNotification(requireContext(), medicine.title, "Time to take your medication: ${medicine.title}", timeInMillis)
            }
        }
    }

    private fun initListener() {

    }

    private fun convertTimeToMillis(time: String): Long {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = sdf.parse(time)

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }

    private fun scheduleNotification(context: Context, title: String, message: String, timeInMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLongClick(medicinesModel: MedicinesModel) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
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
