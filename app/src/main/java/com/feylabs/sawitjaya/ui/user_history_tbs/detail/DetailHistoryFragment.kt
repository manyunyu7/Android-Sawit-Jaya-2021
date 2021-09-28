package com.feylabs.sawitjaya.ui.user_history_tbs.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feylabs.razkyui.RazVerticalStepperAdapter
import com.feylabs.razkyui.RazVerticalStepperAdapter.*
import com.feylabs.razkyui.enum.RazAlertType
import com.feylabs.razkyui.model.VerticalStepperModel
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.databinding.FragmentDetailHistoryBinding
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.UIHelper.renderHtmlToString
import com.feylabs.sawitjaya.utils.UIHelper.showLongToast
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel


class DetailHistoryFragment : BaseFragment() {

    val viewModel: DetailHistoryViewModel by viewModel()
    val photoAdapter by lazy { DetailHistoryPhotoAdapter() }

    var previewState = false //true if preview photo state is visible

    var _binding: FragmentDetailHistoryBinding? = null
    val binding get() = _binding as FragmentDetailHistoryBinding

    lateinit var detailObserver: Observer<Resource<HistoryDetailResponse>?>

    private val args: DetailHistoryFragmentArgs by navArgs()

    override fun initUI() {
        binding.rvPhotos.apply {
            this.adapter = this@DetailHistoryFragment.photoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun initObserver() {
        detailObserver = Observer {
            when (it) {
                is Resource.Success -> {
                    viewGone(binding.includeLoading.root)
                    setupDetailUI(it)
                }
                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                }

                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }

                else -> {
                }//Do Nothing
            }
        }
    }

    private fun setupDetailUI(it: Resource.Success<HistoryDetailResponse>) {
        val mData = it.data
        val userData = mData?.userData
        val rsData = mData?.data

        binding.includeDetailRs.tvEstPriceOld.build(
            title = "Estimasi Harga Lama : ",
            value = "Rp. " + mData?.data?.resultEstPriceOld.toString(),
            hint = "Harga Estimasi Saat Request Dilakukan"
        )

        binding.includeDetailRs.tvEstPriceNow.build(
            title = "Estimasi Harga Saat Ini : ",
            value = "Rp. " + mData?.data?.resultEstPriceNow.toString(),
            hint = "Harga Estimasi Berdasarkan data hari ini"
        )

        binding.includeDetailRs.tvAddress.build(
            title = "Alamat Pengantaran",
            value = rsData?.address.toString()
        )

        binding.includeDetailRs.ivMainImage.loadImageFromURL(
            requireContext(), rsData?.photoPath?.toString()
        )

        if (mData?.driverData != null) {
            val driverData = mData.driverData
            binding.includeDriverInfo.apply {
                this.labelTitle.text = "Informasi Driver"
                this.ivMainImage.loadImageFromURL(requireContext(), driverData.photo_path)
                this.labelUserName.build("Nama : ", driverData.name, showHint = false)
                this.labelUserEmail.build("Email : ", driverData.email, showHint = false)
                this.labelContact.build("Contact : ", driverData.contact, showHint = false)
            }
        }else{
            viewGone(binding.includeDriverInfo.containerContent)
            binding.includeDriverInfo.labelTitle.text= "Belum Ada Driver"
        }

        if (mData?.staffData != null) {
            val staffData = mData.staffData
            binding.includeStaffInfo.apply {
                labelTitle.text = "Informasi Driver"
                ivMainImage.loadImageFromURL(requireContext(), staffData.photo_path)
                labelUserName.build("Nama : ", staffData.name, showHint = false)
                labelUserEmail.build("Email : ", staffData.email, showHint = false)
                labelContact.build("Contact : ", staffData.contact, showHint = true,
                    hint = "Klik Untuk Hubungi Driver")
            }
        }else{
            viewGone(binding.includeStaffInfo.containerContent)
            binding.includeStaffInfo.labelTitle.text= "Belum Ada Staff"
        }

        binding.includeInfoUser.apply {

            userData?.let { usr ->
                tvUserName.text = usr.name
                tvUserContact.text = usr.contact
                tvUserEmail.text = usr.email
                ivProfilePicture.loadImageFromURL(requireContext(), usr.photoPath)
            }

            if (mData?.historyData?.isNotEmpty() == true) {
                val rsData = mData.data
                val historyData = mData.historyData
                val firstHistoryData = historyData.get(0)


                historyData.forEachIndexed { index, item ->

                    val type = RazAlertType.PRIMARY

                    when (item.status) {

                    }

                    binding.razVerticalStepper.adapter.clearData()
                    binding.razVerticalStepper.addData(
                        VerticalStepperModel(
                            id = item.id.toString(),
                            number = index.toString(),
                            title = item.statusDesc,
                            description = (item.desc).renderHtmlToString(),
                        )
                    )
                }


                binding.razVerticalStepper.setStepperInterface(object : VerticalStepperInterface {
                    override fun onclick(model: VerticalStepperModel) {
                        showLongToast(requireContext(), model.number + " " + model.description)
                    }

                })

            } else {
//                binding.alertContainer.build(
//                    "Belum Ada Riwayat",
//                    "Transaksi Ini Belum Diproses",
//                    RazAlertType.PRIMARY
//                )
            }

        }

        mData?.data?.mapToPhotoModel()?.let { listPhoto ->
            photoAdapter.setWithNewData(
                listPhoto
            )
        }
    }

    override fun initAction() {

        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
            viewModel.getDetail(args.rsID)
        }

        photoAdapter.setAdapterInterfacez(object : DetailHistoryPhotoAdapter.RsPhotoItemInterface {
            override fun onclick(model: PhotoListModel?) {
                hideActionBar()
                viewVisible(binding.includePreview.root)
                enabledBackButton(false)

                binding.includePreview.fullscreenContent.loadImageFromURL(
                    requireContext(), model?.url.toString()
                )

                binding.includePreview.btnBack.setOnClickListener {
                    enabledBackButton(true)
                    showActionBar()
                    viewGone(binding.includePreview.root)
                }
            }
        })


    }

    override fun initData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_history, container, false)
        _binding = FragmentDetailHistoryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetail(args.rsID)
        viewModel.detailRsLD.observe(viewLifecycleOwner, detailObserver)
    }


}