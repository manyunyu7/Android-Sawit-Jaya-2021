package com.feylabs.sawitjaya.ui.user_history_tbs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sawitjaya.databinding.FragmentRsScaleBinding
import com.feylabs.sawitjaya.utils.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class RsScaleFragment : BaseFragment() {

    val viewModel: RsScaleViewModel by viewModel()
    private var _binding: FragmentRsScaleBinding? = null
    private val binding get() = _binding as FragmentRsScaleBinding

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
    ): View {
        _binding = FragmentRsScaleBinding.inflate(inflater)
        return binding.root
    }

}