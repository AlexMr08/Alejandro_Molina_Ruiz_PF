package com.example.practica_final.elementos

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
        const val NOTIFICADO_CLIENTE=2
        const val CREADO_CLIENTE=3
    }
}