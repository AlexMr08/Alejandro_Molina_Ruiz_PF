package com.example.practica_final.user

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica_final.databinding.FragmentUserEventBinding

class UserEventFragment : Fragment() {

    private var _binding: FragmentUserEventBinding? = null
    lateinit var menu: Menu
    val ma by lazy {
        activity as UserActivity
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentUserEventBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.fueRv.adapter = ma.adap_evento
        binding.fueRv.layoutManager = LinearLayoutManager(ma)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}