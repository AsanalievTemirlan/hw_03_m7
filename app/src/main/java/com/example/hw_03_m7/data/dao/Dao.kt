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

    @Insert
    fun insertMedicines(medicines: MedicinesModel)

    @Delete
    fun deleteMedicines(medicines: MedicinesModel)

    @Update
    fun updateMedicines(medicines: MedicinesModel)
}