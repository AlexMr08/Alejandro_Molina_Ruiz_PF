package com.example.practica_final.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.practica_final.databinding.FragmentAdminOrderBinding

class AdminOrderFragment : Fragment() {

    private var _binding: FragmentAdminOrderBinding? = null
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

        _binding = FragmentAdminOrderBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.switch1.setOnClickListener {  }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        //esto indica que se muestra en la appbar
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        //esto indica que se quita de la appbar
    }

    override fun onResume() {
        super.onResume()
        (activity as AdminActivity).FAB_manager(0, {})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}