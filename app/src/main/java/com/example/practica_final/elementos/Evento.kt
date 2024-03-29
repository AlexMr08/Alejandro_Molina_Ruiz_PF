package com.example.practica_final.elementos

data class Evento(val id:String?="",
                  val nombre:String?="",
                  val fecha:String?="",
                  val imagen:String?="",
                  val precio:Float?=0.0f,
                  val plazas_totales:Int?=0,
                  var plazas_ocupadas:Int?=0,
                  var disponible:Boolean?=true)