package com.example.practica_final.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.R
import com.example.practica_final.admin.AdminActivity
import com.example.practica_final.databinding.RvAdminBookingBinding
import com.example.practica_final.elementos.Reserva

class AdminBookingAdapter(val lista:List<Reserva>, val con:AdminActivity) : RecyclerView.Adapter<AdminBookingAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvAdminBookingBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvAdminBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        with(holder.bind){
            rabNom.text = elem.nombreCliente
            Glide.with(con).load(elem.imgCliente).circleCrop().placeholder(R.drawable.ic_baseline_account_circle_24).into(rabImg)
            rabFec.text = con.getString(R.string.admin_rserva_fecha,elem.fecha)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}