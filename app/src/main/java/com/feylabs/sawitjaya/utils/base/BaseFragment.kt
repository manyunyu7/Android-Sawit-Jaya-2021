package com.feylabs.sawitjaya.utils.base

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {


    fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    fun viewGone(view: View){
        view.visibility=View.GONE
    }
    fun viewVisible(view: View){
        view.visibility=View.VISIBLE
    }

}