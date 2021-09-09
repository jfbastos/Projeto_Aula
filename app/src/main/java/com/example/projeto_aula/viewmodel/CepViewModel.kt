package com.example.projeto_aula.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.projeto_aula.MyApplication
import com.example.projeto_aula.model.cep.Cep
import com.example.projeto_aula.model.cep.CepRepository
import com.example.projeto_aula.model.EventWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CepViewModel(private val repository: CepRepository) : ViewModel() {
    var cepLiveData: LiveData<List<Cep>> = repository.getCepHistory()
    val cepSearched = MutableLiveData<Cep>()
    val requestError = MutableLiveData<EventWrapper<String>>()

    fun searchCep(cep: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchCep(cep)
                if (response.isSuccessful) {
                    val returned = response.body()!!
                    cepSearched.value = returned
                    insertCep(returned)
                } else {
                    requestError.value = EventWrapper("Cep ${cep} não encontrado")
                }
            } catch (e: Exception) {
                Log.e("Banco", "${e.message}")
                withContext(Dispatchers.Main) {
                    requestError.value = EventWrapper("Erro ao pesquisar o cep")
                }
            }
        }
    }

    private fun insertCep(cep: Cep) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                MyApplication.database!!.cepDao().insertCep(cep)
            }
        }
    }

    fun cepHistory() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                cepLiveData = repository.getCepHistory()
            }
        }
    }

    class CepViewModelFactory(private val repository: CepRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CepViewModel(repository) as T
        }

    }

}