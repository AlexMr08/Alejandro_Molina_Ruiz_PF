package com.example.practica_final.user

import android.os.Bundle
import android.service.controls.Control
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.practica_final.Carta
import com.example.practica_final.ControlDB
import com.example.practica_final.Pedido
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentUserProfileBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
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
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.fupNom.text = ma.usuario.nombre
        Glide.with(ma).load(ma.usuario.img).placeholder(R.drawable.magic_card_back)
            .into(binding.fupImg)
        binding.fupRv.adapter = ma.adaptador_pedidos
        binding.fupRv.layoutManager = LinearLayoutManager(ma)
    }


    override fun onResume() {
        super.onResume()
        //ma.algoraro(0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}