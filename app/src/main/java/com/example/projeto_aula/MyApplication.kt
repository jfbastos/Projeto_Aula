package com.example.projeto_aula

import android.app.Application
import androidx.room.Room
import com.example.projeto_aula.model.AppDatabase

class MyApplication : Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "cep_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}