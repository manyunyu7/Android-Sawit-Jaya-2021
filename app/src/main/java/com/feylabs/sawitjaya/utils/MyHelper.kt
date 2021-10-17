package com.feylabs.sawitjaya.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity


object MyHelper {

    fun openCallerWithNumber(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        context.startActivity(intent)
    }

    fun openGmapsWithDirections(context: Context,lat: Double?, long: Double?) {
        val navigationIntentUri: Uri =
            Uri.parse("google.navigation:q=$lat,$long") //creating intent with latlng

        val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun openSmsWithNumber(context: Context, number: String) {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", "$number")
        context.startActivity(smsIntent)
    }

    fun openWhatsappWithNumber(context: Context, number: String) {
        var sendNumber = number
        if (sendNumber.startsWith("0"))
            sendNumber = sendNumber.replaceFirst("0", "62")
        val url = "https://api.whatsapp.com/send?phone=$sendNumber"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context.startActivity(i)
    }

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

    fun Any.toDoubleStringRoundOff(): String {
        val current = this.toString().toDouble().roundOffDecimal()
        return current?.roundOffDecimal().toString()
    }


}