package com.example.practica_final

import java.io.Serializable

data class Usuario(var nombre:String?=null,
                   var pass:String?=null,
                   var tipo:Int?=0,
                   var id:String?=null,
                   var fecha:String?=null,
                   var img:String?=null,
                   var estado_noti:Int?=0):
    Serializable