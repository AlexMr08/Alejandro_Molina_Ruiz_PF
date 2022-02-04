package com.example.practica_final.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Evento
import com.example.practica_final.databinding.RvAdminEventBinding

class AdminEventAdapter(val lista:List<Evento>, val con:AdminActivity) : RecyclerView.Adapter<AdminEventAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvAdminEventBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvAdminEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        with(holder.bind){
            raeNom.text = elem.nombre
            raePlazas.text = "Aforo: ${elem.plazas_ocupadas}/${elem.plazas_totales}"
            Glide.with(con).load(elem.imagen).into(raeImg)
            raeFecha.text = elem.fecha
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}