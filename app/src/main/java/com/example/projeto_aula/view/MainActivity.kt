package com.example.projeto_aula.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.projeto_aula.R
import com.example.projeto_aula.databinding.ActivityMainBinding
import com.example.projeto_aula.model.User

class MainActivity : AppCompatActivity() {
    companion object{
        val USER_EXTRA = "user"
    }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.user = intent.getSerializableExtra(USER_EXTRA) as User

    }

    override fun onResume() {
        super.onResume()
    }
}