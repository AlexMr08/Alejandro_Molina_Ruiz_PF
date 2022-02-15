package com.example.practica_final.elementos

import android.os.Parcelable
import com.example.practica_final.MainActivity
import com.example.practica_final.aleLib.ControlSP
import kotlinx.parcelize.Parcelize

@Parcelize
data class Moneda(/*val id:Int,*/ val nombre:String, val signo:String, var conversion:Float): Parcelable