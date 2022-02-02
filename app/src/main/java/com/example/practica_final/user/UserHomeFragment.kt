package com.example.practica_final.user

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.practica_final.databinding.FragmentUserHomeBinding


class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
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

        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        binding.fuhSwitch.bringToFront()
        binding.fuhCg.children.forEachIndexed { index, view -> (view as CheckBox).apply { text = ma.categorias[index]; isChecked=true }}
        binding.fuhSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                binding.fuhCg.animate().translationY(0.0f);
                val  ct = object : CountDownTimer(150L,150L){
                    override fun onTick(p0: Long) {

                    }

                    override fun onFinish() {
                        binding.fuhCg.visibility = View.VISIBLE
                    }
                }.start()
            }else{
                binding.fuhCg.animate().translationY(-binding.fuhCg.height.toFloat())
                val  ct = object : CountDownTimer(150L,150L){
                    override fun onTick(p0: Long) {

                    }

                    override fun onFinish() {
                        binding.fuhCg.visibility = View.GONE
                    }
                }.start()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.fuhRv.adapter = ma.adap_carta.also { binding.fuhRv.layoutManager = GridLayoutManager(activity, 2)}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}