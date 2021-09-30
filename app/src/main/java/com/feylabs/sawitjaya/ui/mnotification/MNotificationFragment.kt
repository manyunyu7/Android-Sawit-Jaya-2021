package com.feylabs.sawitjaya.ui.mnotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.databinding.FragmentMNotificationBinding
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel


class MNotificationFragment : BaseFragment() {

    var _binding: FragmentMNotificationBinding? = null
    val binding get() = _binding as FragmentMNotificationBinding

    val viewModel: MNotificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_m_notification, container, false)
        _binding = FragmentMNotificationBinding.bind(view)
        return binding.root
    }

    override fun initUI() {

    }

    override fun initObserver() {
        viewModel.mNotifLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.testing.text = it.data.toString()
                }
                is Resource.Error -> {
                    binding.testing.text=it.data.toString()
                }
                is Resource.Loading -> {

                }
            }
        })
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.fetchNotification(MyPreference(requireContext()).getUserID().toString())
    }


}