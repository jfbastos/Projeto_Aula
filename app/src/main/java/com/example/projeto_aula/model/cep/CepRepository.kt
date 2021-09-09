package com.example.projeto_aula.model.cep

import androidx.lifecycle.LiveData
import com.example.projeto_aula.MyApplication
import com.example.projeto_aula.network.CepAPI
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CepRepository {

    private val api: CepAPI = Retrofit.Builder()
        .baseUrl(CepAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CepAPI::class.java)


    fun getCepHistory(): LiveData<List<Cep>> {
        return MyApplication.database!!.cepDao().cep
    }

    suspend fun fetchCep(cep: String): Response<Cep> {
        return api.fetchCep(cep)
    }
}