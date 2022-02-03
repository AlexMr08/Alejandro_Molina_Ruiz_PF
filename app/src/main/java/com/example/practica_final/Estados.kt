package com.example.practica_final

class EstadoPedido{
    companion object{
        const val CREADO=0
        const val ACEPTADO=1

    }
}
class EstadoNotificaciones{
    companion object{
        const val NOTIFICADO=0
        const val CREADO=1
        const val MODIFICADO_NOMBRE=2
        const val MODIFICADO_DISPONIBLE=3
    }
}