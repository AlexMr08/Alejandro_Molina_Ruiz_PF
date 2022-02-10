package com.example.practica_final.elementos

data class Reserva(val id:String?="",
                   val idCliente:String?="",
                   val idEvento:String?="",
                   val fecha:String?="",
                   var precio:Float?=0f,
                   var nombreEvento:String?="",
                   var imgEvento:String?="",
                   var nombreCliente:String?="",
                   var imgCliente:String?="")