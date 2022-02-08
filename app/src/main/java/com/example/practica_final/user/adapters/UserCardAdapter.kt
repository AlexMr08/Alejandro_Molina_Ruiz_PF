package com.example.practica_final.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Carta
import com.example.practica_final.R
import com.example.practica_final.databinding.RvUserCardBinding
import com.example.practica_final.user.UserActivity
import java.util.*

class UserCardAdapter(val lista:List<Carta>, val con: UserActivity) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>(), Filterable {
    var listaFiltrada = lista
    var filtroCat = true
    var blanco = true
    var negro = true
    var azul = true
    var rojo = true
    var verde = true
    var listaCategorias = listOf(true,true,true,true,true)
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
            Glide.with(con).load(elem.imagen).placeholder(R.drawable.magic_card_back).into(rvUserCardImg)
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
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    listaFiltrada = lista
                } else {
                    val elemetosFiltrados2 = mutableListOf<Carta>()
                    val textoMinuscula = texto.lowercase(Locale.ROOT)
                    for (e in lista) {
                        val nombreMinuscula = e.nombre?.lowercase(Locale.ROOT)
                        if(nombreMinuscula!!.contains(textoMinuscula)){
                            elemetosFiltrados2.add(e)
                        }
                    }
                    listaFiltrada = elemetosFiltrados2
                }

                if (filtroCat){
                    listaFiltrada = listaFiltrada.filter{
                        val index = Carta.categorias.indexOf(it.categoria)
                        listaCategorias[index]
                    }
                }
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