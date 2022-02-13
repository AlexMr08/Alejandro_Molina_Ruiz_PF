package com.example.practica_final.user


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.practica_final.aleLib.ControlSP
import com.example.practica_final.databinding.ActivityUserSettingsBinding
import com.example.practica_final.elementos.Moneda

data class Rate(val USD:Double, val EUR:Double)

class UserSettingsActivity : AppCompatActivity() {
    val binding by lazy { ActivityUserSettingsBinding.inflate(layoutInflater) }
    val controlSP by lazy { ControlSP(this) }
    val lista_monedas by lazy { intent.getSerializableExtra("MONEDAS") as List<Moneda> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val id_moneda_sel = controlSP.moneda_sel
        actualizarAdapter(lista_monedas.map { it.nombre })
        binding.spinner.setSelection(id_moneda_sel)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    controlSP.moneda_sel = position
                }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                    //hay que ponerlo pero no lo hemos usado :(
                }
            }
    }

    fun actualizarAdapter(lista: List<String>){
        val spi_adap = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        spi_adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter=spi_adap
    }


}