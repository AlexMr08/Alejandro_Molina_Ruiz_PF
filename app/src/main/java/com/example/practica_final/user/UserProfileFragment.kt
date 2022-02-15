package com.example.practica_final.user

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentUserProfileBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class UserProfileFragment : Fragment() {
    data class rep(val nom: String, val num: Float, val color: Int)

    private var _binding: FragmentUserProfileBinding? = null
    lateinit var menu: Menu
    lateinit var pieData: PieData
    val ma by lazy {
        activity as UserActivity
    }
    lateinit var reporte : List<rep>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

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
        val pieEntries = mutableListOf<PieEntry>()
        val cartas_usuario = ma.lista_pedidos.size
        binding.fupCartasUsu.text = cartas_usuario.toString()
        reporte = ma.generarReporte()
        if (ma.lista_pedidos.isEmpty()){
            binding.fupCard.visibility = View.VISIBLE
            binding.fupBtnVolver.setOnClickListener {
                ma.navController.navigate(R.id.userHomeFragment)
            }
        }else{
            binding.fupCard.visibility = View.INVISIBLE
            reporte.map {
                pieEntries.add(PieEntry(it.num, it.nom))
            }
            val label = "Categorias de mis cartas"
            val pieDataSet = PieDataSet(pieEntries, label)
            pieDataSet.valueTextSize = 16f
            pieDataSet.colors = reporte.map { it.color }
            pieDataSet.valueTextColor = Color.parseColor("#FFFFFF")
            pieData = PieData(pieDataSet)
            pieData.setDrawValues(true)
            val pieChart = ma.findViewById<PieChart>(R.id.fup_pieChart)
            pieChart.data = pieData
            pieChart.legend.isEnabled = false
            pieChart.description.text = ""
        }
        
        binding.fupNom.text = getString(R.string.nombre_usuario,ma.usuario.nombre)
        Glide.with(ma).load(ma.usuario.img).circleCrop().placeholder(R.drawable.magic_card_back)
            .into(binding.fupImg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}