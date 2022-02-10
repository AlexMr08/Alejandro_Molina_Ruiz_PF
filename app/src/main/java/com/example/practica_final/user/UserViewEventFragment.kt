package com.example.practica_final.user

import android.media.metrics.Event
import android.os.Bundle
import android.service.controls.Control
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.practica_final.R
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.databinding.FragmentUserViewEventBinding
import com.example.practica_final.elementos.Evento
import com.example.practica_final.elementos.Reserva
import java.text.SimpleDateFormat
import java.util.*


class UserViewEventFragment : Fragment() {

    private var _binding: FragmentUserViewEventBinding? = null
    lateinit var menu: Menu
    val ma by lazy { activity as UserActivity }
    val elem by lazy{ ma.evento_sel }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentUserViewEventBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        Glide.with(ma).load(elem.imagen).transform(CenterCrop(), RoundedCorners(10)).into(binding.fuveImg)
        binding.fuveNom.text = elem.nombre
        binding.fuveNumAforo.text = ma.getString(R.string.evento_aforo,elem.plazas_ocupadas,elem.plazas_totales)
        binding.fuveNumPrecio.text = ma.getString(R.string.evento_precio_rv, elem.precio)
        binding.button.setOnClickListener {
            val fecha = Calendar.getInstance()
            val formateador = SimpleDateFormat("yyyy-MM-dd")
            val hoy = formateador.format(fecha.time)
            val id_res = ControlDB.rutaResEventos.push().key
            val evento = Reserva(id_res,ma.controlSP.id,elem.id,hoy, elem.precio)
            var suma = elem.plazas_ocupadas!!+1
            ControlDB.rutaResEventos.child(id_res?:"").setValue(evento)
            ControlDB.rutaEvento.child(elem.id?:"").child("plazas_ocupadas").setValue(suma)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        //esto indica que se muestra en la appbar
        this.menu.findItem(R.id.app_bar_search).setVisible(false)
    }


    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        //esto indica que se quita de la appbar
        menu.findItem(R.id.app_bar_search).setVisible(false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}