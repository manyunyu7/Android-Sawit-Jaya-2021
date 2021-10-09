package com.feylabs.sawitjaya.ui.user_history_tbs.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentUpdateRsBinding
import com.feylabs.sawitjaya.utils.base.BaseFragment


class UpdateRsFragment : BaseFragment() {

    var _binding: FragmentUpdateRsBinding? = null
    val binding get() = _binding as FragmentUpdateRsBinding

    override fun initUI() {
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateRsBinding.inflate(inflater)
        return binding.root
    }


}