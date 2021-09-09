package com.feylabs.sawitjaya.utils

import timber.log.Timber
import java.text.NumberFormat
import java.util.*

object BusinessHelper {

    fun getEstimatedPrice(
        weight: Double,
        margin: Double,
        price: Double
    ): String {

        val localeID = Locale("in", "ID")
        val formatRupiah =
            NumberFormat.getCurrencyInstance(localeID)
        val weightNetto = weight - (margin * weight)
        val weightReducedWithMargin = weight - weightNetto


        Timber.d("--helper calculated weight:${weight}, margin : $margin, price:$price")
        Timber.d("--helper calculated :($price) * ($weight - ($weight*$margin))")
        val finalPrice = (price) * (weight - (weight*margin))
        Timber.d("--helper calculated result : $finalPrice")

        val result = formatRupiah.format(finalPrice)

        val finalResult =
            "Berat Kotor : ${weight} \n" +
                    "Berat Bersih : ${weightReducedWithMargin} \n" +
                    "Margin  : ${margin} \n" +
                    "Harga Sawit : ${price}\n" +
                    "Estimasi Harga Jual Sawit anda adalah $result, hasil ini dapat berubah sesuai kondisi ketika penjemputan"

        return result
    }
}