package com.example.practica_final.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.practica_final.R
import com.example.practica_final.admin.adapters.AdminBookingAdapter
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.databinding.FragmentAdminViewEventBinding
import com.example.practica_final.elementos.Evento
import com.example.practica_final.elementos.Reserva
import com.example.practica_final.elementos.Usuario
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class AdminViewEventFragment : Fragment() {

    private var _binding: FragmentAdminViewEventBinding? = null
    lateinit var menu: Menu
    lateinit var adap: AdminBookingAdapter
    val ma by lazy {
        activity as AdminActivity
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminViewEventBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val index = ma.evento_sel
        val elem = ma.lista_eventos[index]
        val lista = generarLista(elem)
        refreshUI(elem, lista.size)

        adap = AdminBookingAdapter(lista, ma)
        binding.faveRv.adapter = adap
        binding.faveRv.layoutManager = LinearLayoutManager(ma)
    }

    override fun onResume() {
        super.onResume()
        ma.FAB_manager(0, {})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun generarLista(elem: Evento): MutableList<Reserva> {
        var lista = mutableListOf<Reserva>()
        ControlDB.rutaResEventos.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val snap = snapshot.getValue(Reserva::class.java) ?: Reserva()
                if (snap.idEvento == elem.id) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val sem = CountDownLatch(1)
                        ControlDB.rutaUsuario.child(snap.idCliente!!)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val usu = snapshot.getValue(Usuario::class.java) ?: Usuario()
                                    snap.imgCliente = usu.img
                                    snap.nombreCliente = usu.nombre
                                    sem.countDown()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        sem.await()
                        lista.add(snap)
                        ma.runOnUiThread {
                            adap.notifyDataSetChanged()
                            refreshUI(elem,lista.size)
                        }
                    }


                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return lista
    }

    fun refreshUI(elem : Evento, num : Int){
        Glide.with(ma).load(elem.imagen).transform(CenterCrop(), RoundedCorners(50))
            .placeholder(R.drawable.ic_baseline_location_on_24).into(binding.faveImg)
        binding.faveNom.text = elem.nombre
        binding.faveAforo.text =
            ma.getString(R.string.evento_aforo, num, elem.plazas_totales)
        binding.favePre.text = ma.getString(R.string.evento_precio_rv, elem.precio)
        binding.faveFec.text = elem.fecha
    }
}