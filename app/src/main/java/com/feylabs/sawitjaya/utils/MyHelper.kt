package com.feylabs.sawitjaya.utils

import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.net.URL

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


}