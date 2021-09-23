package com.feylabs.sawitjaya.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {


    abstract fun initUI()
    abstract fun initObserver()
    abstract fun initAction()
    abstract fun initData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initAction()
        initUI()
        initData()
    }

    fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    fun viewGone(view: View) {
        view.visibility = View.GONE
    }

    fun viewVisible(view: View) {
        view.visibility = View.VISIBLE
    }


}