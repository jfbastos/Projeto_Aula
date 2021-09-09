// To parse the JSON, install Klaxon and do:
//
//   val cep = Cep.fromJson(jsonString)

package com.example.projeto_aula.model.cep

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cep")
data class Cep(
    @PrimaryKey val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val ddd: String,
)