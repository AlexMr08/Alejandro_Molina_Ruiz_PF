package com.example.practica_final.elementos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Usuario(var nombre:String?="",
                   var correo:String?="",
                   var pass:String?="",
                   var tipo:Int?=0,
                   var id:String?="",
                   var fecha:String?="",
                   var img:String?=""):
    Parcelable