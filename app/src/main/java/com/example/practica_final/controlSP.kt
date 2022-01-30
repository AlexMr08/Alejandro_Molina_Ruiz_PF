package com.example.practica_final

import android.content.Context
import android.content.SharedPreferences

class controlSP(c : Context, sp : SharedPreferences) {
        val contexto = c
        val sp = sp
        var tipo = sp.getInt(c.getString(R.string.sp_tipo),1)
        var id = sp.getString(c.getString(R.string.sp_usuario),"")

    fun cambiarSPPerfil(idn:String, tip:Int){
        with(sp.edit()){
            putString(contexto.getString(R.string.sp_usuario),idn)
            putInt(contexto.getString(R.string.sp_tipo),tip)
            commit()
        }
    }
    fun borrarSPPerfil(){
        with(sp.edit()){
            with(sp.edit()){
                putString(contexto.getString(R.string.sp_usuario),"")
                putInt(contexto.getString(R.string.sp_tipo),1)
                commit()
            }
        }
    }
}