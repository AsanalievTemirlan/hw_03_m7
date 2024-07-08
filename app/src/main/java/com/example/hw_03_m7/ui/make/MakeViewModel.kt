package com.example.hw_03_m7.ui.make


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun insertMedicines(medicinesModel: MedicinesModel) {
        viewModelScope.launch {
            repository.insert(medicinesModel)
        }
    }

    fun updateMedicine(medicinesModel: MedicinesModel) {
        viewModelScope.launch {
            repository.update(medicinesModel)
        }
    }

    fun getMedicineId(id: Int): LiveData<MedicinesModel> {
        return repository.getMedicineById(id)
    }

}