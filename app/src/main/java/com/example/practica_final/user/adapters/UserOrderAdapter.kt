package com.example.practica_final.user.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.R
import com.example.practica_final.databinding.RvUserOrderBinding
import com.example.practica_final.elementos.Carta
import com.example.practica_final.elementos.EstadoPedido
import com.example.practica_final.elementos.Pedido
import com.example.practica_final.user.UserActivity

class UserOrderAdapter(val lista:List<Pedido>, val con:UserActivity) : RecyclerView.Adapter<UserOrderAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvUserOrderBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvUserOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        val precio = elem.precio?:0f
        val precio_final = precio.times((con.moneda_sel).conversion)
        with(holder.bind){
            Glide.with(con).load(elem.imgCarta).placeholder(R.drawable.magic_card_back).into(rvProfileImg)
            ruoCard.strokeColor = Color.parseColor(Carta.colores[Carta.categorias.indexOf(elem.categoria)])
            rvProfileCat.setColorFilter(Color.parseColor(Carta.colores[Carta.categorias.indexOf(elem.categoria)]))
            rvProfilePre.text = con.getString(R.string.carta_precio,precio_final,con.moneda_sel.signo)
            rvProfileNom.text = elem.nombreCarta.toString()
            if (elem.estado == EstadoPedido.CREADO){
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