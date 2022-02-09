package com.example.practica_final.aleLib

import android.content.Context
import com.example.practica_final.R

class ControlSP(c: Context) {
    val contexto = c
    val ID = "com.example.practica_final"
    val shared_name = "${ID}_sp"
    val sp = contexto.getSharedPreferences(shared_name, 0)

    //Tipo de usuario
    var tipo: Int
        get() = sp.getInt(
            contexto.getString(R.string.sp_tipo),
            contexto.resources.getInteger(R.integer.sp_tipo_def)
        ) ?: contexto.resources.getInteger(R.integer.sp_tipo_def)
        set(value) = sp.edit().putInt(contexto.getString(R.string.sp_tipo), value).apply()

    //Id del usuario
    var id: String
        get() = sp.getString(
            contexto.getString(R.string.sp_usuario),
            ""
        ) ?: ""
        set(value) = sp.edit().putString(
            contexto.getString(R.string.sp_usuario), value).apply()

    //Ultima comprobacion API monedas
    var ultimaComp: String
        get() = sp.getString(
            contexto.getString(R.string.sp_ultimaComp),
            contexto.getString(R.string.sp_ultimaComp_def)
        ) ?: contexto.getString(R.string.sp_ultimaComp_def)

        set(value) = sp.edit().putString(
            contexto.getString(R.string.sp_ultimaComp),
            value).apply()

    var eur_usd: Float
        get() = sp.getString(
            contexto.getString(R.string.sp_eur_usd),
            contexto.resources.getString(R.string.sp_eur_usd_def)
        )?.toFloat() ?: contexto.resources.getString(R.string.sp_eur_usd_def).toFloat()

        set(value) = sp.edit().putString(contexto.getString(R.string.sp_tipo), value.toString()).apply()

    fun borrarSPPerfil() {
        with(sp.edit()) {
            putString(contexto.getString(R.string.sp_usuario), "")
            putInt(contexto.getString(R.string.sp_tipo), 1)
            commit()
        }
    }
}