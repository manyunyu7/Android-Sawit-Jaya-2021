package com.feylabs.sawitjaya.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ContextUtils.getActivity


object UIHelper {

    fun showLongToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast(context: Context) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }

    fun ImageView.loadImageFromURL(
        context: Context,
        url: String? = "",
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(url)
            .placeholder(thumbnailsType.value)
            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

    fun ImageView.loadImage(
        context: Context,
        drawable: Int,
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(drawable)
            .placeholder(thumbnailsType.value)
            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }


    fun setColorStatus(status: String): Int {
        return when (status) {
            "0" -> R.color.bootstrapRed
            "1" -> R.color.colorGreen
            "3" -> R.color.colorWaiting
            "4" -> R.color.colorNeoBlue
            "2" -> R.color.colorPurple
            else -> {
                R.color.black
            }
        }
    }

    fun String.renderHtmlToString(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return (Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)).toString()
        } else {
            return (Html.fromHtml(this)).toString()
        }
    }

    enum class ThumbnailsType(val value: Int) {
        ADD_PHOTO_1(R.drawable.ic_add_photo_placeholder),
        LOADING_1(R.drawable.ic_placeholder),
    }

    fun Fragment.hideActionBar() {
        activity?.actionBar?.hide()
        (getActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    fun Fragment.showActionBar() {
        activity?.actionBar?.show()
        (getActivity() as AppCompatActivity).supportActionBar?.hide()
    }


}