package com.feylabs.sawitjaya.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat

object MyHelper {

    fun getByteArrayFromImageURL(url: String): String? {
        try {
            val imageUrl = URL(url)
            val ucon = imageUrl.openConnection()
            val `is` = ucon.getInputStream()
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var read = 0
            while (`is`.read(buffer, 0, buffer.size).also { read = it } != -1) {
                baos.write(buffer, 0, read)
            }
            baos.flush()
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return null
    }


    fun renderHTML(text: String): String {
        var returnValue: Spanned? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            returnValue = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            returnValue = Html.fromHtml(text)
        }
        return returnValue.toString()
    }

    fun Double.roundOffDecimal(): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(this).toDouble()
    }


}