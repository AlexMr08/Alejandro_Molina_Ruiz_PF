package com.example.practica_final.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.practica_final.R
import com.example.practica_final.aleLib.ControlSP
import com.example.practica_final.databinding.ActivityAdminSettingsBinding

class AdminSettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAdminSettingsBinding.inflate(layoutInflater) }
    private lateinit var controlSP : ControlSP
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        controlSP = ControlSP(this)
        binding.switch3.isChecked = controlSP.tema
        binding.switch3.setOnCheckedChangeListener { _, b ->
            val tema = if (b){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
            controlSP.tema=b
            AppCompatDelegate.setDefaultNightMode(tema)
        }
    }
}