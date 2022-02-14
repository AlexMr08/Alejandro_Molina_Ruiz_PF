package com.example.practica_final.user.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.practica_final.R
import com.example.practica_final.aleLib.AleLib
import com.example.practica_final.databinding.RvUserEventBinding
import com.example.practica_final.elementos.Evento
import com.example.practica_final.user.UserActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class UserEventAdapter(val lista:List<Evento>, val con: UserActivity) : RecyclerView.Adapter<UserEventAdapter.ViewHolder>(){

    class ViewHolder(val bind:RvUserEventBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvUserEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = lista[position]
        val precio_final = elem.precio?.times((con.moneda_sel).conversion)
        val signo = con.moneda_sel.signo
        with(holder.bind){
            AleLib.glide_img(con,elem.imagen!!,R.drawable.ic_baseline_location_on_24,rueImg,14)
            rueNom.text = elem.nombre
            ruePre.text = con.getString(R.string.evento_precio, precio_final, signo )
            rueAfo.text = con.getString(R.string.evento_aforo_rv, elem.plazas_ocupadas, elem.plazas_totales)
            rueFec.text = elem.fecha
            rueCard.setOnClickListener {
                con.evento_sel = elem
                con.navController.navigate(R.id.userViewEventFragment)
            }
            val fecha = LocalDate.parse(elem.fecha, DateTimeFormatter.ofPattern("d/M/yyyy"))
            val hoy = LocalDate.now()
            rueCard.isClickable =
                (fecha.isEqual(hoy) || fecha.isAfter(hoy) && elem.plazas_ocupadas!=elem.plazas_totales)

            if (fecha.isBefore(hoy)){
                rueAfo.text = ("No disponible")
            }else{

            }

            if (elem.plazas_ocupadas==elem.plazas_totales){
                rueSo.visibility = View.VISIBLE
            }else{
                rueSo.visibility = View.GONE
            }


        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}