package com.example.practica_final.aleLib

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.practica_final.R

class AleLib {
    companion object{
        val MILIS_IN_DAY = 24*60*60*1000
        fun glide_img(con : Context, img:String, ph:Int, view: ImageView, radio:Int){
            Glide.with(con).load(img).transform(CenterCrop(), RoundedCorners(radio)).placeholder(ph).into(view)
        }
    }
}

class VolleySingleton(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}