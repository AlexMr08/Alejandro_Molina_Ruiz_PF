package com.example.practica_final.elementos

import android.R
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView


data class Carta(val id:String?="",
                 val nombre:String?="",
                 val categoria:String?="",
                 val imagen:String?="",
                 val precio:Float?=0.0f,
                 var disponible:Boolean?=true){
    companion object{
        val categorias = listOf<String>("Negro","Blanco","Azul","Rojo","Verde")
        val colores = listOf<String>("#000000", "#BCBCBC", "#333c87","#9b111e","#287233")
    }
}

