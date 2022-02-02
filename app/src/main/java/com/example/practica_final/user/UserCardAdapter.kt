package com.example.practica_final.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Carta
import com.example.practica_final.MainActivity
import com.example.practica_final.R
import com.example.practica_final.databinding.RvUserCardBinding

class UserCardAdapter(val lista:List<Carta>, val con:UserActivity) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>(), Filterable {

    var listaFiltrada = lista

    class ViewHolder(val bind:RvUserCardBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvUserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val elem = listaFiltrada[position]
        with(holder.bind){
            rvUserCardNom.text = elem.nombre
            rvUserCardPre.text = con.getString(R.string.carta_precio_rv,elem.precio)
            Glide.with(con).load(elem.imagen).into(rvUserCardImg)
            rvUserCardCl.setOnClickListener {
                con.carta_sel = elem
                con.navController.navigate(R.id.userViewCardFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                listaFiltrada = listaFiltrada.filter { it.disponible == true }
                val filterResults = FilterResults()
                filterResults.values = listaFiltrada
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, resultado: FilterResults?) {
                listaFiltrada = resultado?.values as MutableList<Carta>
                notifyDataSetChanged()
            }
        }
    }
}