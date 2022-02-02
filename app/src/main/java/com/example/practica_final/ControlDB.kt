package com.example.practica_final

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ControlDB {
    val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    val stoRef by lazy { FirebaseStorage.getInstance().reference }

    companion object{
        val rutaUsuario = dbRef.child("tienda").child("usuarios")
        val rutaEvento = dbRef.child("tienda").child("eventos")
        val rutacartas = dbRef.child("tienda").child("cartas")
        val rutaResEventos = dbRef.child("tienda").child("reserva_eventos")
        val rutaResCartas = dbRef.child("tienda").child("reserva_cartas")

        val stoRutaCartas = stoRef.child("tienda").child("cartas")
        val stoRutaUsuarios = stoRef.child("tienda").child("usuarios")
        val stoRutaEvento = stoRef.child("tienda").child("eventos")
    }
}