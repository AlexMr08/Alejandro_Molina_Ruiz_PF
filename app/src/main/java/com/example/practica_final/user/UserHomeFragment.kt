package com.example.practica_final.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SearchView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.practica_final.elementos.Carta
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentUserHomeBinding


class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
    lateinit var menu: Menu
    val ma by lazy {
        activity as UserActivity
    }
    var categorias = mutableListOf(true, true, true, true, true)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        refreshUI()
        binding.fuhSwitch.setOnClickListener {
            refreshSwitch()
        }

        binding.fuhCg.children.forEachIndexed { index, view ->
            (view as CheckBox).apply {
                text = Carta.categorias[index]; isChecked = true
                setOnClickListener{
                    val cb = (it as CheckBox)
                    categorias[index] = cb.isChecked
                    ma.adap_carta.listaCategorias = categorias
                    refreshFilter()
                }
            }
        }

        binding.fuhRv.adapter =
            ma.adap_carta.also { binding.fuhRv.layoutManager = GridLayoutManager(activity, 2) }
    }

    override fun onResume() {
        super.onResume()
        //ma.algoraro(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        this.menu.findItem(R.id.app_bar_search).setVisible(true)
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        menu.findItem(R.id.app_bar_search).setVisible(false)
    }

    fun refreshFilter() {
        val query = (menu.findItem(R.id.app_bar_search).actionView as SearchView).query
        ma.adap_carta.filter.filter(query)
    }

    fun refreshButton(check: Boolean) {
        binding.fuhCg.children.forEach { it.isEnabled = check }
        refreshFilter()
    }

    fun refreshSwitch(){
        refreshUI()
        val check = binding.fuhSwitch.isChecked
        ma.adap_carta.filtroCat = check
        refreshFilter()
    }

    fun refreshUI(){
        if (binding.fuhSwitch.isChecked){
            binding.fuhCg.children.forEach { it.isEnabled=true }
        }else{
            binding.fuhCg.children.forEach { it.isEnabled=false }
        }
    }
}