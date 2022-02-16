package com.example.practica_final.aleLib

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practica_final.R

class ControlNotif {
    companion object{
        fun generarNotificacion(con:Context, id_noti:Int,contenido:String,titulo:String,destino:Class<*>) {
            val idcanal = con.getString(R.string.id_canal)
            val iconolargo = BitmapFactory.decodeResource(
                con.resources,
                R.drawable.logov2
            )
            val actividad = Intent(con,destino)
            actividad.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
            val pendingIntent=PendingIntent.getActivity(con,0,actividad,PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(con, idcanal)
                .setLargeIcon(iconolargo)
                .setSmallIcon(R.drawable.drawing)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setSubText("sistema de información")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            with(NotificationManagerCompat.from(con)){
                notify(id_noti,notification)
            }
        }
    }
}