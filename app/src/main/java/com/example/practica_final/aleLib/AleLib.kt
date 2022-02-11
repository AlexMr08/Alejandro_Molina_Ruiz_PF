package com.example.practica_final.aleLib

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.practica_final.R

class AleLib {
    companion object{
        fun imgGlide(con:Activity, img:String, ph:Int, View:ImageView){
            Glide.with(con).load(img).transform(CenterCrop(), RoundedCorners(10)).placeholder(
                ph).into(View)
        }
    }
}