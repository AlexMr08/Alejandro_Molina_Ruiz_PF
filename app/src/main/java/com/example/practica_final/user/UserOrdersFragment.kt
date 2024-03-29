package com.example.practica_final.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentUserOrdersBinding

class UserOrdersFragment : Fragment() {

    private var _binding: FragmentUserOrdersBinding? = null
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

        _binding = FragmentUserOrdersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
    }


    override fun onResume() {
        super.onResume()
        if (ma.lista_pedidos.size==0){
            binding.fuoCard.visibility = View.VISIBLE
            binding.fuoCard.setOnClickListener {
                ma.navController.navigate(R.id.userHomeFragment)
            }
        }else{
            binding.fuoCard.visibility = View.INVISIBLE
            binding.fuoRv.adapter = ma.adap_pedidos
            binding.fuoRv.layoutManager = LinearLayoutManager(ma)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}