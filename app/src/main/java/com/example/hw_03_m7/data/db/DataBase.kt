package com.example.hw_03_m7.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hw_03_m7.data.dao.Dao
import com.example.hw_03_m7.data.model.MedicinesModel

@Database(entities = [MedicinesModel::class], version = 5)
abstract class DataBase : RoomDatabase() {
    abstract fun medicinesDao(): Dao
}

