package com.example.projeto_aula.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_aula.R
import com.example.projeto_aula.databinding.ActivityBuscaCepBinding
import com.example.projeto_aula.model.auth.User
import com.example.projeto_aula.model.cep.CepRepository
import com.example.projeto_aula.view.adapter.CepHistoryAdapter
import com.example.projeto_aula.viewmodel.CepViewModel

class BuscaCepActivity : AppCompatActivity() {
    companion object {
        val USER_EXTRA = "user"
    }

    private lateinit var binding: ActivityBuscaCepBinding
    private lateinit var viewModel: CepViewModel
    private val cepAdapter = CepHistoryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busca_cep)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_busca_cep)

        val user = intent.getSerializableExtra(USER_EXTRA) as User
        this.title = user.name

        viewModel = ViewModelProvider(
            this, CepViewModel.CepViewModelFactory(
                CepRepository()
            )
        ).get(CepViewModel::class.java)

        binding.cepList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cepAdapter
        }

        binding.buttonSearch.setOnClickListener {
            val cep = binding.cepInputText.text.toString()
            if (cep.isNotBlank()) {
                viewModel.searchCep(cep)
            }
        }

        viewModel.cepSearched.observe(this) {   cep->
            binding.cep = cep
        }

        viewModel.cepLiveData.observe(this) { historic ->
            historic?.let {
                cepAdapter.update(it)
            }
        }

        viewModel.requestError.observe(this) { erro ->
            erro.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cepHistory()
    }
}