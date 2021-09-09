package com.example.projeto_aula.network

import com.example.projeto_aula.model.cep.Cep
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CepAPI {
    companion object {
        const val BASE_URL = "https://viacep.com.br/ws/"
    }

    @GET("{cep}/json")
    suspend fun fetchCep(@Path("cep") cep: String): Response<Cep>
}