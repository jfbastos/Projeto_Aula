package com.example.projeto_aula.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_aula.R
import com.example.projeto_aula.databinding.CepItemBinding
import com.example.projeto_aula.model.cep.Cep

class CepHistoryAdapter(private val items: ArrayList<Cep>) :
    RecyclerView.Adapter<CepHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemBinding = CepItemBinding.bind(view)
        fun bind(cep: Cep) {
            itemBinding.textCep.text = cep.cep
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cep_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(listaDeCeps: List<Cep>) {
        items.clear()
        items.addAll(listaDeCeps)
        notifyDataSetChanged()
    }


}