package com.example.practica_final.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Carta
import com.example.practica_final.EstadoPedido
import com.example.practica_final.Pedido
import com.example.practica_final.R
import com.example.practica_final.databinding.RvUserProfileCardBinding

class UserProfileCardAdapter(val lista:List<Pedido>, val con:Context) : RecyclerView.Adapter<UserProfileCardAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvUserProfileCardBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvUserProfileCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        with(holder.bind){
            Glide.with(con).load(elem.imgCarta).into(rvProfileImg)
            rvProfileNom.text = elem.nombreCarta.toString()
            if (elem.estado==EstadoPedido.CREADO){
                rvProfileChecked.setImageResource(R.drawable.ic_round_shopping_cart_24)
            }else{
                rvProfileChecked.setImageResource(R.drawable.ic_baseline_check_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}