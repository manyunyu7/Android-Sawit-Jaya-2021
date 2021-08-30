package com.feylabs.sawitjaya.base

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {


    fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

}