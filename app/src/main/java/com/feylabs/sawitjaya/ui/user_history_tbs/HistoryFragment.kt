package com.feylabs.sawitjaya.ui.user_history_tbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.databinding.BsActionHistoryBinding
import com.feylabs.sawitjaya.databinding.FragmentHistoryBinding
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.rs.request.RsDetailFragment
import com.feylabs.sawitjaya.ui.user_history_tbs.detail.DetailHistoryFragmentArgs
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.viewmodel.ext.android.viewModel
import com.google.android.material.chip.Chip
import timber.log.Timber


class HistoryFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        adapterHistory.clear()
    }

    private val menuNavController: NavController? by lazy { activity?.findNavController(R.id.nav_host_fragment_content_user_main_menu) }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding as FragmentHistoryBinding

    private val adapterHistory by lazy { HistoryAdapter() }

    private lateinit var historyObserver: Observer<Resource<HistoryPagingModel>>

    val viewModel: HistoryViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()

    val bottomSheetDialog by lazy {
        BottomSheetDialog(
            requireActivity(),
            R.style.Theme_MaterialComponents_BottomSheetDialog
        )
    }

    private val bsBinding by lazy {
        BsActionHistoryBinding.bind(
            LayoutInflater.from(requireContext())
                .inflate(
                    R.layout.bs_action_history,
                    requireActivity().findViewById(R.id.bottom_action)
                )
        )
    }

    override fun initUI() {
    }

    override fun initObserver() {
        historyObserver = Observer {
            when (it) {
                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }
                is Resource.Success -> {
                    val dataSize = it.data?.data?.size
                    //hide desc/error view
                    viewGone(binding.includeDesc.root)
                    binding.includeDesc.tvDesc.text = ""

                    if (dataSize == 0) {
                        viewGone(binding.includeLoading.root)
                        binding.includeDesc.apply {
                            root.visibility = View.VISIBLE
                            tvDesc.text = "Belum Ada Data"
                            viewVisible(this.root)
                        }
                    } else {
                        it.data?.data?.let { data ->
                            adapterHistory.addData(data)
                        }
                        viewGone(binding.includeLoading.root)
                    }

                }

                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                    binding.includeDesc.apply {
                        root.visibility = View.VISIBLE
                        tvDesc.text = it.data.toString() + " " + it.message
                        viewVisible(this.root)
                    }
                }
            }
        }
    }

    override fun initAction() {
    }

    override fun initData() {
        var userId = MyPreference(requireContext()).getUserID()
        if (MyPreference(requireContext()).getRole() == "3") {
            userId = "all"
        }
        viewModel.getRSByUser(userId.toString())
        viewModel.historyDataLD.observe(viewLifecycleOwner, historyObserver)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        _binding = FragmentHistoryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheet()
        setupRecyclerView(LAYOUT_TYPE.LINEAR)
        setupChip()

        binding.group.setOnCheckedChangeListener { groupz, checkedId ->
            val ids: List<Int> = groupz.checkedChipIds
            for (id in ids) {
                val chip: Chip = groupz.findViewById(id)
                Timber.d("checked_chip ${chip.text}")
            }
        }


        adapterHistory.setInterface(object : HistoryAdapter.HistoryItemInterface {
            override fun onclick(model: HistoryPagingModel.HistoryModel) {
                bottomSheetDialog.show()
                bsBinding.btnDetailOrEdit.setOnClickListener {
                    goToFragmentDetail(model.id)
                    bottomSheetDialog.dismiss()
                }
                bsBinding.bottomAction.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }
            }

        })

    }

    private fun goToFragmentDetail(id: Int) {
        val directions =
            HistoryFragmentDirections.actionHistoryFragmentToDetailHistoryFragment(
                id.toString()
            )
        menuNavController?.navigate(directions)
    }


    private fun setupChip() {
        val userID = MyPreference(requireContext()).getUserID().toString()
        val group = binding.group
        for (i in 0 until group.size) {
            val indexed = group[i]
            val chip: Chip = group[i].findViewById(indexed.id)
            if (chip.isSelected) {
                showToast(i.toString())
            }
            chip.setOnClickListener {
                adapterHistory.clear()
                when (i) {
                    0 -> {
                        viewModel.getRSByUser(userID = userID, status = null)
                    }
                    1 -> {
                        viewModel.getRSByUser(userID = userID, status = "3")
                    }
                    2 -> {
                        viewModel.getRSByUser(userID = userID, status = "4")
                    }
                    3 -> {
                        viewModel.getRSByUser(userID = userID, status = "2")
                    }
                    4 -> {
                        viewModel.getRSByUser(userID = userID, status = "1")
                    }

                }
            }
        }
    }


    private fun setupBottomSheet() {
        bottomSheetDialog.setContentView(bsBinding.root)
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

