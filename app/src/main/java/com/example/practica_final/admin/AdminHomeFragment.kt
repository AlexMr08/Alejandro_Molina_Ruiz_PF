package com.example.practica_final.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica_final.databinding.FragmentAdminHomeBinding

class AdminHomeFragment : Fragment() {
    private val ma by lazy { activity as AdminActivity }
    private var _binding: FragmentAdminHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        ma.FAB_manager(1,{})
    }

    override fun onStart() {
        super.onStart()
        binding.fahRv.adapter = ma.adap_carta.also { binding.fahRv.layoutManager = LinearLayoutManager(activity) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}