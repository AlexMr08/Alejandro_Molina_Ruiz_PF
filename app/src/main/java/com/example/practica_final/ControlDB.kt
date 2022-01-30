package com.example.practica_final

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ControlDB {
    companion object{
        val dbRef by lazy { FirebaseDatabase.getInstance().reference }
        val stoRef by lazy { FirebaseStorage.getInstance().reference }
        val rutaUsuario = dbRef.child("tienda").child("usuarios")
        val rutaEvento = dbRef.child("tienda").child("eventos")
        val rutacartas = dbRef.child("tienda").child("cartas")
        val rutaResEventos = dbRef.child("tienda").child("reserva_eventos")
        val rutaResCartas = dbRef.child("tienda").child("reserva_cartas")
    }
}