package com.example.practica_final.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Evento
import com.example.practica_final.R
import com.example.practica_final.databinding.RvUserEventBinding
import com.example.practica_final.user.UserActivity

class UserEventAdapter(val lista:List<Evento>, val con: UserActivity) : RecyclerView.Adapter<UserEventAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvUserEventBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvUserEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        with(holder.bind){
            Glide.with(con).load(elem.imagen).centerCrop().placeholder(R.drawable.ic_baseline_event_24).into(rueImg)
            rueNom.text = elem.nombre
            ruePre.text = con.getString(R.string.evento_precio,elem.precio)
            rueFec.text = elem.fecha

        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}