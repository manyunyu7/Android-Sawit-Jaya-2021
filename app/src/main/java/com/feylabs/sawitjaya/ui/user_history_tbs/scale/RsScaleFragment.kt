package com.feylabs.sawitjaya.ui.user_history_tbs.scale

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sawitjaya.databinding.FragmentRsScaleBinding
import com.feylabs.sawitjaya.utils.MyHelper.roundOffDecimal
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.data.remote.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.databinding.ItemRsScaleBinding
import com.feylabs.sawitjaya.utils.DialogUtils


class RsScaleFragment : BaseFragment() {

    val viewModel: RsScaleViewModel by viewModel()
    private var _binding: FragmentRsScaleBinding? = null
    private val binding get() = _binding as FragmentRsScaleBinding

    private val mAdapter by lazy { RsScaleAdapter() }

    private val args: RsScaleFragmentArgs by navArgs()

    override fun initUI() {
        hideActionBar()

        binding.toolbarTitle.text = "Data Timbangan Transaksi #${args.rsID}"

        mAdapter.setAdapterInterfacez(object : RsScaleAdapter.RsScaleItemInterface {
            override fun onclick(model: RsScaleModel?, binding: ItemRsScaleBinding) {
                if (args.rsStatus != "5") {
                    showRestrictedStatusDialog()
                } else {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_are_you_sure),
                        message =
                        "Anda Yakin Ingin Menghapus Data Berat Ini ?",
                        positiveAction = Pair("OK", {
                            deleteScaleItem(model?.id.toString())
                        }),
                        negativeAction = Pair("Batal", {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }

            }

            override fun onSurface(model: RsScaleModel?, binding: ItemRsScaleBinding) {

            }
        })

        // Check if current user role is regulat user/not a staff
        if (MyPreference(requireContext()).getRole() == "3") {
            binding.containerInsertNew.visibility = View.GONE
        } else {
            // if current user role is staff

            // if current status is not at "Sedang Ditimbang/Onsite"
            if (args.rsStatus != "5") {
                showRestrictedStatusDialog()
                binding.etNewResult.setOnClickListener {
                    showRestrictedStatusDialog()
                }
                binding.btnSave.setOnClickListener {
                    showRestrictedStatusDialog()
                }

                // if current status is at "Sedang Ditimbang"
            } else {
                binding.btnSave.setOnClickListener {
                    val text = binding.etNewResult.text.toString()
                    if (text.isEmpty()) {
                        binding.etNewResult.error = getString(R.string.error_message_empty_input)
                    } else {
                        DialogUtils.showCustomDialog(
                            context = requireContext(),
                            title = getString(R.string.title_modal_are_you_sure),
                            message =
                            "Anda Yakin Akan Menginput Data Hasil Timbangan dengan berat $text ke transaksi ini ?",
                            positiveAction = Pair("OK", {
                                viewModel.storeScale(
                                    args.rsID,
                                    text,
                                    MyPreference(requireContext()).getUserID().toString()
                                )
                            }),
                            autoDismiss = true,
                            buttonAllCaps = false
                        )
                    }
                }

            }
        }

        binding.toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_arrow_back_24
            )
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.rvScale.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showRestrictedStatusDialog() {
        DialogUtils.showCustomDialog(
            context = requireContext(),
            title = getString(R.string.title_modal_attention),
            message = "Saat Ini Data Berat Tidak Dapat Diubah, Untuk mengubah/menambah data status transaksi harus berada di 'Sedang Ditimbang'",
            positiveAction = Pair("OK", {}),
            autoDismiss = true,
            buttonAllCaps = false
        )
    }

    private fun deleteScaleItem(rsID: String) {
        viewModel.deleteScaleById(rsID)
    }

    override fun initObserver() {
        viewModel.storeScaleLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    binding.etNewResult.text.clear()
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.message_modal_success_input_rs_scale),
                        positiveAction =
                        Pair(getString(R.string.label_Ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fetchScaleData(args.rsID)
                }
                is Resource.Error -> {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_error_occured),
                        message = getString(R.string.message_modal_error_input_rs_scale) + "\n"
                                + it.message.toString(),
                        positiveAction = Pair("OK", {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fetchScaleData(args.rsID)
                }
            }
        })

        viewModel.deleteScaleLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    binding.etNewResult.text.clear()
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.message_modal_success_delete_rs_scale),
                        positiveAction =
                        Pair(getString(R.string.label_Ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fetchScaleData(args.rsID)
                }
                is Resource.Error -> {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_error_occured),
                        message = getString(R.string.message_modal_error_delete_rs_scale) + " ${it.message}",
                        positiveAction = Pair("OK", {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fetchScaleData(args.rsID)
                }
            }
        })

        viewModel.scaleLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val tempList = mutableListOf<RsScaleModel>()
                    var totalWeight: Double? = 0.0
                    tempList.clear()
                    it.data?.resData?.let { listData ->

                        totalWeight = listData.totalWeight.roundOffDecimal()

                        // add data to recyclerview
                        listData.data.forEachIndexed { index, data ->
                            tempList.add(
                                RsScaleModel(
                                    id = data.id.toString(),
                                    result = data.result.toString().toDouble(),
                                    inputedAt = data.createdAt,
                                    inputedBy = data.staff_name
                                )
                            )
                            mAdapter.setWithNewData(tempList)
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                    if (tempList.size == 0) {
                        binding.tvTotal.text =
                            "Belum Ada Data Timbangan"
                    } else {
                        binding.tvTotal.text =
                            "Total Berat Saat Ini\n $totalWeight Kg "
                    }
                }
                is Resource.Error -> {
                }
            }
        })
    }

    override fun initAction() {
        binding.etNewResult.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 1) {
                    binding.etNewResult.setError(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }

    override fun initData() {
        viewModel.fetchScaleData(args.rsID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRsScaleBinding.inflate(inflater)
        return binding.root
    }

}