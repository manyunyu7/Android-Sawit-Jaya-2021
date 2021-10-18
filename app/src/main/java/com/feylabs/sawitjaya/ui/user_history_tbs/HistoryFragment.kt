package com.feylabs.sawitjaya.ui.user_history_tbs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.remote.response.HistoryDataResponse
import com.feylabs.sawitjaya.databinding.BsActionHistoryBinding
import com.feylabs.sawitjaya.databinding.FragmentHistoryBinding
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.ui.user_history_tbs.detail.DetailHistoryFragmentDirections
import com.feylabs.sawitjaya.utils.DialogUtils
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

    var currentStatus: String? = null // save current chip status

    var isLoading = true

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding as FragmentHistoryBinding

    private val adapterHistory by lazy { HistoryAdapter() }

    private lateinit var historyObserver: Observer<Resource<HistoryDataResponse?>>

    val viewModel: HistoryViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()


    var userId = ""

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
        setupBottomSheet()
        setupRecyclerView(LAYOUT_TYPE.LINEAR)
        setupChip()
        binding.progressBar.visibility = View.GONE
    }

    override fun initObserver() {
        historyObserver = Observer {
            when (it) {
                is Resource.Loading -> {
                    isLoading = true
                    viewVisible(binding.includeLoading.root)
                }
                is Resource.Success -> {
                    isLoading = false
                    val dataSize = it.data?.data?.size
                    //hide desc/error view
                    viewGone(binding.includeDesc.root)
                    binding.includeDesc.tvDesc.text = ""

                    it.data?.data?.let { data ->
                        adapterHistory.addData(data.toMutableList())
                    }
                    viewGone(binding.includeLoading.root)

                    if (adapterHistory.itemCount == 0) {
                        binding.includeDesc.apply {
                            root.visibility = View.VISIBLE
                            tvDesc.text = "Belum Ada Data"
                            viewVisible(this.root)
                        }
                    }

                }

                is Resource.Error -> {
                    isLoading = false
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
        userId = MyPreference(requireContext()).getUserID().toString()
        if (MyPreference(requireContext()).getRole() == "1") {
            userId = "all"
        }
        getDatas(false, 1)
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

        binding.group.setOnCheckedChangeListener { groupz, checkedId ->
            val ids: List<Int> = groupz.checkedChipIds
            for (id in ids) {
                val chip: Chip = groupz.findViewById(id)
                Timber.d("checked_chip ${chip.text}")
            }
        }

        adapterHistory.setInterface(object : HistoryAdapter.HistoryItemInterface {
            override fun onclick(model: HistoryDataResponse.Data) {
                bottomSheetDialog.show()

                bsBinding.btnQrCode.setOnClickListener {
                    goToFragmentQR(model)
                    bottomSheetDialog.dismiss()
                }

                bsBinding.btnSeeInvoicw.setOnClickListener {
                    if (model.status != "1") {
                        DialogUtils.showCustomDialog(
                            context = requireContext(),
                            title = getString(R.string.title_modal_attention),
                            message = getString(R.string.message_modal_restrict_invoice_from_detail),
                            positiveAction = Pair(getString(R.string.dialog_ok), {
                            }),
                            negativeAction = Pair(getString(R.string.dialog_cancel), {}),
                            autoDismiss = true,
                            buttonAllCaps = false
                        )
                    } else {
                        goToFragmentInvoice(model.id.toString())
                    }
                }

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

    private fun goToFragmentQR(model: HistoryDataResponse.Data) {
        val directions = HistoryFragmentDirections.actionHistoryFragmentToShowRsQrCodeFragment(
            model.id.toString(), model.rsCode, model.userName
        )
        findNavController().navigate(directions)
    }

    private fun goToFragmentDetail(id: Int) {
        val directions =
            HistoryFragmentDirections.actionHistoryFragmentToDetailHistoryFragment(
                id.toString()
            )
        menuNavController?.navigate(directions)
    }

    private fun goToFragmentInvoice(id: String) {
        val directions =
            HistoryFragmentDirections.actionHistoryFragmentToInvoiceFragment(
                id
            )
        findNavController().navigate(directions)
    }


    private fun setupChip() {
        val group = binding.group
        for (i in 0 until group.size) {
            val indexed = group[i]
            val chip: Chip = group[i].findViewById(indexed.id)
            if (chip.isSelected) {
                showToast(i.toString())
            }
            chip.setOnClickListener {
                adapterHistory.page = 1
                adapterHistory.clear()
                when (i) {
                    0 -> {
                        currentStatus = null
                        viewModel.getRSByUser(userID = userId, status = null)
                    }
                    1 -> {
                        currentStatus = "3"
                        viewModel.getRSByUser(userID = userId, status = "3")
                    }
                    2 -> {
                        currentStatus = "4"
                        viewModel.getRSByUser(userID = userId, status = "4")
                    }
                    3 -> {
                        currentStatus = "2"
                        viewModel.getRSByUser(userID = userId, status = "2")
                    }
                    4 -> {
                        currentStatus = "1"
                        viewModel.getRSByUser(userID = userId, status = "1")
                    }

                }
            }
        }
    }


    private fun setupBottomSheet() {
        bottomSheetDialog.setContentView(bsBinding.root)
    }

    private fun setupRecyclerView(type: LAYOUT_TYPE) {
        adapterHistory.page = 1
        binding.rvRs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val layoutManager = binding.rvRs.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (adapterHistory.itemCount < 10)
                    adapterHistory.page = 1

                // If current item of the page is not multiple of 10
                // because my pagination backend is allowed 10 each page
                var isEndOfPage = false
                if (adapterHistory.itemCount % 10 == 0) {
                    isEndOfPage = true
                }

                if (!isLoading && isEndOfPage) {
                    if (visibleItemCount + pastVisibleItem == adapterHistory.itemCount) {
                        adapterHistory.page++
                        getDatas(onRefresh = false, adapterHistory.page)
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })


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

    private fun getDatas(onRefresh: Boolean, page: Int) {
        viewModel.getRSByUser(userId, page = page, status = currentStatus)
    }

    fun isLastVisible(rv: RecyclerView): Boolean {
        val layoutManager = rv.layoutManager as LinearLayoutManager
        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems: Int = rv.adapter!!.itemCount
        return pos >= numItems - 1
    }

    enum class LAYOUT_TYPE(val int: Int) {
        LINEAR(LinearLayoutManager.VERTICAL),
        GRID(GridLayoutManager.HORIZONTAL),
    }
}

