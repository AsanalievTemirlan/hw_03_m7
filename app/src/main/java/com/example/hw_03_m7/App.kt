package com.example.hw_03_m7

import android.app.Application
import androidx.room.Room
import com.example.hw_03_m7.data.db.DataBase
import com.example.hw_03_m7.data.db.MIGRATION_1_2
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var appDatabase: DataBase
    }

    override fun onCreate() {
        super.onCreate()
        appDatabase = Room.databaseBuilder(
            applicationContext,
            DataBase::class.java,
            "note.database"
        ).addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }
}
