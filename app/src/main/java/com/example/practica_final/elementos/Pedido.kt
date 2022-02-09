package com.example.practica_final.elementos

data class Pedido(val id:String?="",
                  val idCliente:String?="",
                  val idCarta:String?="",
                  val fecha:String?="",
                  var estado:Int?=0,
                  var precio:Float?=0f,
                  var nombreCarta:String?="",
                  var imgCarta:String?="",
                  var nombreCliente:String?="",
                  var imgCliente:String?="",
                  var estadoNotificacion:Int?=EstadoNotificaciones.CREADO)