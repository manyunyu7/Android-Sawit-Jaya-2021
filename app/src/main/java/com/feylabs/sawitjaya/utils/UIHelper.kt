package com.feylabs.sawitjaya.utils

import android.content.Context
import android.widget.Toast

object UIHelper {

    fun showLongToast(context: Context,text:String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast(context: Context) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }


}