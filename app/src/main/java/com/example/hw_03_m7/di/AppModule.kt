package com.example.hw_03_m7.di

import android.content.Context
import androidx.room.Room
import com.example.hw_03_m7.data.dao.Dao
import com.example.hw_03_m7.data.db.DataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRoomDataBase(@ApplicationContext context: Context): DataBase =
        Room.databaseBuilder(context, DataBase::class.java, "DataBase").fallbackToDestructiveMigration().allowMainThreadQueries().build()

    @Provides
    fun provideDao(loveDataBase: DataBase): Dao {
        return loveDataBase.medicinesDao()
    }
}