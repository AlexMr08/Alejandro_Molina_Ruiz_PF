package com.example.practica_final.admin


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica_final.*
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.databinding.RvAdminOrderBinding
import com.example.practica_final.elementos.Carta
import com.example.practica_final.elementos.EstadoNotificaciones
import com.example.practica_final.elementos.EstadoPedido
import com.example.practica_final.elementos.Pedido

class AdminOrdersAdapter(val lista:List<Pedido>, val con: AdminActivity) : RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder>(),
    Filterable {
    private var listaFiltrada = lista

    var check = false
    class ViewHolder(val bind: RvAdminOrderBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = listaFiltrada[position]
        with(holder.bind){
            refreshUI(elem,holder)
            Glide.with(con).load(elem.imgCarta).placeholder(R.drawable.magic_card_back).into(rapImgCarta)
            rapCard.strokeColor = Color.parseColor(Carta.colores[Carta.categorias.indexOf(elem.categoria)])
            rapNomCarta.text = elem.nombreCarta
            rapPrecio.text = con.getString(R.string.carta_precio,elem.precio,"€")
            rapCliente.text = con.getString(R.string.nom_cliente,elem.nombreCliente)
            rapAceptar.setOnClickListener {
                elem.estado= EstadoPedido.ACEPTADO
                con.adap_pedido.notifyItemChanged(position)
                ControlDB.rutaResCartas.child(elem.id?:"").child("estado").setValue(EstadoPedido.ACEPTADO)
                ControlDB.rutaResCartas.child(elem.id?:"").child("estadoNotificacion").setValue(EstadoNotificaciones.CREADO_CLIENTE)
                    holder.bind.rapAceptar.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (check){
                    listaFiltrada = lista.filter { it.estado==EstadoPedido.CREADO }
                }else{
                    listaFiltrada = lista
                }

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

    fun refreshUI(elem: Pedido, holder: ViewHolder){
        if (elem.estado== EstadoPedido.ACEPTADO){
            holder.bind.rapAceptar.visibility = View.GONE
        }else if (elem.estado== EstadoPedido.CREADO){
            holder.bind.rapAceptar.visibility = View.VISIBLE
        }
    }
}