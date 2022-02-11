package com.example.practica_final.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.practica_final.R
import com.example.practica_final.admin.adapters.AdminBookingAdapter
import com.example.practica_final.databinding.FragmentAdminViewEventBinding

class AdminViewEventFragment : Fragment() {

    private var _binding: FragmentAdminViewEventBinding? = null
    lateinit var menu: Menu
    val ma by lazy {
        activity as AdminActivity
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentAdminViewEventBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val elem = ma.evento_sel
        Glide.with(ma).load(elem.imagen).transform(CenterCrop(), RoundedCorners(50)).placeholder(R.drawable.ic_baseline_location_on_24).into(binding.faveImg)
        binding.faveNom.text = elem.nombre
        binding.faveRv.adapter = AdminBookingAdapter(ma.lista_reservas.filter { it.idEvento==elem.id },ma)
        binding.faveRv.layoutManager = LinearLayoutManager(ma)
        binding.faveAforo.text = ma.getString(R.string.evento_aforo,elem.plazas_ocupadas,elem.plazas_totales)
        binding.favePre.text = ma.getString(R.string.evento_precio_rv,elem.precio)
        binding.faveFec.text = elem.fecha

    }

    override fun onResume() {
        super.onResume()
        ma.FAB_manager(0,{})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}