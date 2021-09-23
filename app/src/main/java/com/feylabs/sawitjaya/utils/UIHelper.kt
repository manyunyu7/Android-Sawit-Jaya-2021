package com.feylabs.sawitjaya.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R

object UIHelper {

    fun showLongToast(context: Context,text:String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast(context: Context) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }

    fun ImageView.loadImageFromURL(context: Context, url:String){
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_add_photo_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

    fun setColorStatus(status:String) : Int{
        return when (status) {
            "0" -> R.color.bootstrapRed
            "1" -> R.color.colorGreen
            "3" -> R.color.colorWaiting
            "4" -> R.color.colorNeoBlue
            "2" -> R.color.colorPurple
            else -> {R.color.black}
        }
    }


}