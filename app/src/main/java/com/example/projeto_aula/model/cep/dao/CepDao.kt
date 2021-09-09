package com.example.projeto_aula.model.cep.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projeto_aula.model.cep.Cep

@Dao
interface CepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCep(cep : Cep)

    @get: Query("SELECT * FROM Cep")
    val cep : LiveData<List<Cep>>

    @Query("DELETE FROM Cep")
    fun delete()
}