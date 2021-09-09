package com.example.projeto_aula.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projeto_aula.model.cep.Cep
import com.example.projeto_aula.model.cep.dao.CepDao


@Database(entities = [Cep::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cepDao(): CepDao
}