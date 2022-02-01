package com.example.practica_final.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentAdminEventBinding

class AdminEventFragment : Fragment() {

    private var _binding: FragmentAdminEventBinding? = null
    private val ma by lazy { activity as AdminActivity }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        ma.FAB_manager(2,{})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}