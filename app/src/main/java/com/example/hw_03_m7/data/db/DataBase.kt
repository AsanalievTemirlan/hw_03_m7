package com.example.hw_03_m7.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hw_03_m7.data.dao.Dao
import com.example.hw_03_m7.data.model.MedicinesModel

@Database(entities = [MedicinesModel::class], version = 2)
abstract class DataBase : RoomDatabase() {
    abstract fun medicinesDao(): Dao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Пример изменения: добавление нового столбца
        database.execSQL("ALTER TABLE medicines ADD COLUMN description TEXT")
    }
}