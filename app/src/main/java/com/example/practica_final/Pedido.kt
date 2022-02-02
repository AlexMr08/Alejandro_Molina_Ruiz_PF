package com.example.practica_final

data class Pedido(val id:String, val idCliente:String, val idCarta:String, val fecha:String, var estado:Int, val nombreCarta:String?="", val imgCarta:String?="")