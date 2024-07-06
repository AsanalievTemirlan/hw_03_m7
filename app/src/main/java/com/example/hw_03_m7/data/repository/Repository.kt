package com.example.hw_03_m7.data.repository

import androidx.lifecycle.LiveData
import com.example.hw_03_m7.data.dao.Dao
import com.example.hw_03_m7.data.model.MedicinesModel
import javax.inject.Inject

class Repository @Inject constructor(private val dao: Dao) {

    fun getAll(): LiveData<List<MedicinesModel>> = dao.getAllMedicines()

    suspend fun insert(medicinesModel: MedicinesModel) {
        dao.insertMedicines(medicinesModel)
    }

    suspend fun delete(medicinesModel: MedicinesModel) {
        dao.deleteMedicines(medicinesModel)
    }
}
