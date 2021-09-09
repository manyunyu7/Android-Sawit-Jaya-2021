package com.feylabs.sawitjaya.ui.user_history_tbs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.databinding.HistoryFragmentBinding
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var _binding: HistoryFragmentBinding? = null
    val binding get() = _binding as HistoryFragmentBinding

    val adapterHistory by lazy { HistoryAdapter() }


    val viewModel: HistoryViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.history_fragment, container, false)
        _binding = HistoryFragmentBinding.bind(view)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(LAYOUT_TYPE.LINEAR)

        val id = MyPreference(requireContext()).getUserID()
        viewModel.getRSByUser(id.toString())

        viewModel.historyDataLD.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    showToast("Loading")
                }
                is Resource.Success -> {
                    showToast("${it.data?.data?.size}")
                    it.data?.data?.let { data ->
                        adapterHistory.addData(data)
                        adapterHistory.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    showToast(it.message.toString())
                }
            }
        })
    }

    private fun setupRecyclerView(type: LAYOUT_TYPE) {
        binding.rvRs.apply {
            when (type) {
                LAYOUT_TYPE.GRID -> {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                }
                LAYOUT_TYPE.LINEAR -> {
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
            adapter = adapterHistory
        }
    }

    enum class LAYOUT_TYPE(val int: Int) {
        LINEAR(LinearLayoutManager.VERTICAL),
        GRID(GridLayoutManager.HORIZONTAL),
    }
}

