package com.example.practica_final.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.Pedido
import com.example.practica_final.databinding.RvAdminOrderBinding

class AdminOrdersAdapter(lista:List<Pedido>, val con: Context) : RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder>(),
    Filterable {
    private var listaFiltrada = lista

    class ViewHolder(val bind: RvAdminOrderBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = listaFiltrada[position]
        with(holder.bind){
            Glide.with(con).load(elem.imgCarta).into(rvAdminPedidoImg)
            rvAdminPedidoNomCarta.text = elem.nombreCarta
            rvAdminPedidoPrecio.text = elem.nombreCarta
        }
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {

                val filterResults = FilterResults()
                filterResults.values = listaFiltrada
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, resultado: FilterResults?) {
                listaFiltrada = resultado?.values as MutableList<Pedido>
                notifyDataSetChanged()
            }
        }
    }
}