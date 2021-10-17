package com.feylabs.sawitjaya.ui.user_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentUserPageBinding
import com.feylabs.sawitjaya.ui.base.BaseFragment


class UserPageFragment : BaseFragment() {

    private var _binding: FragmentUserPageBinding? = null
    private val binding get() = _binding as FragmentUserPageBinding

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
        _binding = FragmentUserPageBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

}