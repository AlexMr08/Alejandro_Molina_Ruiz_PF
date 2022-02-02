package com.example.practica_final.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Carta
import com.example.practica_final.ControlDB
import com.example.practica_final.R
import com.example.practica_final.databinding.RvAdminCardBinding

class AdminCardAdapter(val lista:List<Carta>, val con:Context) : RecyclerView.Adapter<AdminCardAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvAdminCardBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvAdminCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        with(holder.bind){
            rvacNom.text = elem.nombre
            rvacSwi.isChecked = elem.disponible?:false
            Glide.with(con).load(elem.imagen).placeholder(R.drawable.magic_card_back).into(rvacImg)
            rvacSwi.setOnCheckedChangeListener { button, check ->
                ControlDB.rutacartas.child(elem.id?:"").child("disponible").setValue(check)
            }
        }
    }


    override fun getItemCount(): Int {
        return lista.size
    }
}