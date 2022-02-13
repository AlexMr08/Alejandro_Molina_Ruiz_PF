package com.example.practica_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Creador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creador)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}