package com.example.practica_final

import java.io.Serializable

data class Usuario(var nombre:String?="",
                   var correo:String?="",
                   var pass:String?="",
                   var tipo:Int?=0,
                   var id:String?="",
                   var fecha:String?="",
                   var img:String?="",
                   var estado_noti:Int?=0):
    Serializable