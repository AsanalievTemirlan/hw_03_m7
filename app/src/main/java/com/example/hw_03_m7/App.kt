package com.example.hw_03_m7

import android.app.Application
import androidx.room.Room
import com.example.hw_03_m7.data.db.DataBase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}
