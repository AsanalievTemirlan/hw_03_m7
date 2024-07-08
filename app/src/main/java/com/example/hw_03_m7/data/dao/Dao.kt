package com.example.hw_03_m7.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hw_03_m7.data.model.MedicinesModel


@Dao
interface Dao {

    @Query("SELECT * FROM model")
    fun getAllMedicines():  LiveData<List<MedicinesModel>>

    @Query("SELECT * FROM model WHERE id = :id")
    fun getMedicineById(id: Int): LiveData<MedicinesModel>

    @Insert
    suspend fun insertMedicines(medicines: MedicinesModel)

    @Delete
    suspend fun deleteMedicines(medicines: MedicinesModel)

    @Update
    suspend fun updateMedicines(medicines: MedicinesModel)
}