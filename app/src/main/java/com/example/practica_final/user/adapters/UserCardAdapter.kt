package com.example.practica_final.user.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.practica_final.elementos.Carta
import com.example.practica_final.R
import com.example.practica_final.databinding.RvUserCardBinding
import com.example.practica_final.user.UserActivity
import java.util.*

class UserCardAdapter(val lista: List<Carta>, val con: UserActivity) :
    RecyclerView.Adapter<UserCardAdapter.ViewHolder>(), Filterable {
    var listaFiltrada = lista
    var filtroCat = true
    var listaCategorias = listOf(true, true, true, true, true)

    class ViewHolder(val bind: RvUserCardBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvUserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = listaFiltrada[position]
        val precio_final = elem.precio?.times((con.moneda_sel).conversion)
        val signo = con.moneda_sel.signo
        with(holder.bind) {
            rvUserCardNom.text = elem.nombre
            rvUserCardPre.text = con.getString(R.string.carta_precio_rv, precio_final, signo)
            rueCard.strokeColor = Color.parseColor(Carta.colores[Carta.categorias.indexOf(elem.categoria)])
            rueCard.rippleColor = ColorStateList.valueOf(Color.parseColor(Carta.colores[Carta.categorias.indexOf(elem.categoria)]))
            imageView5.setColorFilter(Color.parseColor(Carta.colores[Carta.categorias.indexOf(elem.categoria)]))
            Glide.with(con).load(elem.imagen).transform(RoundedCorners(20))
                .placeholder(R.drawable.magic_card_back).into(rvUserCardImg)
            rueCard.setOnClickListener {
                con.carta_sel = elem
                con.navController.navigate(R.id.action_userHomeFragment_to_userViewCardFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val texto = p0.toString()
                listaFiltrada = if (texto.isEmpty()) {
                    lista
                } else {
                    val elemetosFiltrados2 = mutableListOf<Carta>()
                    val textoMinuscula = texto.lowercase(Locale.ROOT)
                    for (e in lista) {
                        val nombreMinuscula = e.nombre?.lowercase(Locale.ROOT)?:""
                        if (nombreMinuscula.contains(textoMinuscula)) {
                            elemetosFiltrados2.add(e)
                        }
                    }
                    elemetosFiltrados2
                }

                if (filtroCat) {
                    listaFiltrada = listaFiltrada.filter {
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