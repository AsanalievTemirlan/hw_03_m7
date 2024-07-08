package com.example.hw_03_m7.ui.medicines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hw_03_m7.data.model.MedicinesModel
import com.example.hw_03_m7.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicinesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getAll(): LiveData<List<MedicinesModel>> {
        Log.e("TAG" , "MedicinesViewModel ${repository.getAll()}")
        return repository.getAll()
    }
    suspend fun delete(medicinesModel: MedicinesModel){
        return repository.delete(medicinesModel)
    }
}
