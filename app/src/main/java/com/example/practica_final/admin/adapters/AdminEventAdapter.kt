package com.example.practica_final.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.elementos.Evento
import com.example.practica_final.R
import com.example.practica_final.admin.AdminActivity
import com.example.practica_final.aleLib.AleLib
import com.example.practica_final.databinding.RvAdminEventBinding

class AdminEventAdapter(val lista:List<Evento>, val con: AdminActivity) : RecyclerView.Adapter<AdminEventAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvAdminEventBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvAdminEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        with(holder.bind){
            raeNom.text = elem.nombre
            raePlazas.text = con.getString(R.string.evento_aforo,elem.plazas_ocupadas,elem.plazas_totales)
            raePre.text = con.getString(R.string.evento_precio_rv,elem.precio, "€")
            Glide.with(con).load(elem.imagen).transform(CenterCrop(), RoundedCorners(20)).placeholder(R.drawable.ic_baseline_location_on_24).into(raeImg)
            raeFecha.text = elem.fecha
            raeCl.setOnClickListener {
                con.evento_sel = position
                con.navController.navigate(R.id.adminViewEventFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}