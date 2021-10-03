package com.feylabs.sawitjaya.ui.mnotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

    val adapter by lazy { MNotificationAdapter() }

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
        binding.rvNotif.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        adapter.setAdapterInterfacez(object : MNotificationAdapter.MNotificationInterface {
            override fun onclick(model: MNotificationModel?) {
                showToast(model?.title.toString())
            }

        })
    }

    override fun initObserver() {
        viewModel.mNotifLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.srl.isRefreshing=false
                    val data = it.data
                    if (data != null) {
                        if (data.size > 0) {
                            val tempList = mutableListOf<MNotificationModel>()
                            tempList.clear()
                            it.data.forEachIndexed { index, dataz ->
                                tempList.add(
                                    MNotificationModel(
                                        user_id = dataz.userId.toString(),
                                        rs_id = dataz.rsId,
                                        title = dataz.title,
                                        desc = dataz.desc,
                                        message = dataz.message,
                                        image = "todo",
                                        type = dataz.type,
                                        isRead = dataz.isRead,
                                        createdAt = dataz.createdAt.toString(),
                                    )
                                )
                            }
                            adapter.setWithNewData(tempList)
                        }
                    }
                }
                is Resource.Error -> {
                    binding.srl.isRefreshing=false

                }
                is Resource.Loading -> {
                    binding.srl.isRefreshing=true
                }
            }
        })
    }

    override fun initAction() {
        binding.srl.setOnRefreshListener {
            fetchNotificationData()
        }
    }

    override fun initData() {
        fetchNotificationData()
    }

    private fun fetchNotificationData() {
        viewModel.fetchNotification(MyPreference(requireContext()).getUserID().toString())
    }


}