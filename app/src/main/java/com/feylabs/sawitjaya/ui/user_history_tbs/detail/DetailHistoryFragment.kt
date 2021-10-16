package com.feylabs.sawitjaya.ui.user_history_tbs.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.razkyui.RazVerticalStepperAdapter.*
import com.feylabs.razkyui.enum.RazAlertType
import com.feylabs.razkyui.model.VerticalStepperModel
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.databinding.FragmentDetailHistoryBinding
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.UIHelper.renderHtmlToString
import com.feylabs.sawitjaya.utils.UIHelper.showLongToast
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.databinding.LayoutDialogSpinnerBinding
import com.feylabs.sawitjaya.utils.DialogUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.viewModel
import android.content.DialogInterface
import android.widget.Adapter
import androidx.appcompat.app.AlertDialog
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.utils.MyHelper.roundOffDecimal
import com.feylabs.sawitjaya.utils.MyHelper.toDoubleStringRoundOff


class DetailHistoryFragment : BaseFragment(), OnMapReadyCallback {

    private val viewModel: DetailHistoryViewModel by viewModel()
    private val photoAdapter by lazy { DetailHistoryPhotoAdapter() }

    private var _binding: FragmentDetailHistoryBinding? = null
    private val binding get() = _binding as FragmentDetailHistoryBinding

    private lateinit var mMap: GoogleMap

    lateinit var detailObserver: Observer<Resource<HistoryDetailResponse>?>

    private val args: DetailHistoryFragmentArgs by navArgs()

    val statusLiveData = MutableLiveData<String>()

    override fun initUI() {

        //hide change status if role is user
        if (MyPreference(requireContext()).getRole() == "3") {
            binding.containerChangeStatus.visibility = View.GONE
        }

        setupStatusSpinner()
        binding.includeAdditionalMenu.apply {
            btnScale.isEnabled = false
        }
        binding.rvPhotos.apply {
            this.adapter = this@DetailHistoryFragment.photoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    override fun initAction() {
        binding.includeAdditionalMenu.apply {
            btnLiveTrack.setOnClickListener {
                showToast("Fitur Ini Belum Tersedia")
            }

            btnInvoice.setOnClickListener {
                goToFragmentInvoice()
            }

            btnDetail.setOnClickListener {
                goToFragmentUpdate()
            }

            btnChat.setOnClickListener {
                goToFragmentChat()
            }
        }

        binding.fab.setOnClickListener {
            goToFragmentChat()
        }

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


    override fun initObserver() {

        viewModel.changeRsStatusLD.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    viewGone(binding.includeLoading.root)
                    viewModel.getDetail(args.rsID)
                }
                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                }

                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }
            }
        })
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
            }
        }

        statusLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                binding.includeAdditionalMenu.btnScale.isEnabled = false
            } else {
                binding.includeAdditionalMenu.btnScale.isEnabled = true
                binding.includeAdditionalMenu.btnScale.setOnClickListener { v ->
                    if (it != "5") {
                        goToFragmentRsScale(it)
                    } else {
                        goToFragmentRsScale(it)
                    }
                }

            }
        })

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

        viewModel.mapLatLngLv.observe(viewLifecycleOwner, Observer {
            if (it != null && viewModel.isMapReady.value == true) {
                setupPinOnMap(it)
            }
        })

    }

    private fun setupPinOnMap(location: LatLng) {
        mMap.addMarker(MarkerOptions().position(location).title("Lokasi Penjemputan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
    }

    private fun setupDetailUI(it: Resource.Success<HistoryDetailResponse>) {
        val mData = it.data
        val userData = mData?.userData
        val rsData = mData?.data
        val priceData = mData?.price

        binding.includeDetailRs.apply {

            tvDate.text = rsData?.createdAt

            tvEstPriceOld.build(
                title = "Estimasi Harga Lama : ",
                value = "Rp. " + mData?.data?.resultEstPriceOld.toString(),
                hint = "Harga Estimasi Saat Request Dilakukan"
            )

            tvEstPriceNow.build(
                title = "Estimasi Harga Saat Ini : ",
                value = "Rp. " + mData?.data?.resultEstPriceNow.toString(),
                hint = "Harga Estimasi Berdasarkan data hari ini"
            )

            tvUserName.text = rsData?.userName

            tvAddress.build(
                title = "Alamat Pengantaran",
                value = rsData?.address.toString()
            )
            tvOldMargin.value(
                value = rsData?.estMargin?.toDouble()?.times(100)?.roundOffDecimal().toString()
            )

            tvCurrentMargin.value(
                value = priceData?.margin?.times(100)?.roundOffDecimal().toString()
            )

            tvOldPrice.value(
                value = "Rp. ${rsData?.estPrice.toString()}"
            )
            tvCurrentPrice.value(
                value = "Rp. ${priceData?.price.toString()}"
            )

            tvTotalWeight.value(
                value = "${
                    mData?.totalWeight?.toString()?.toDoubleOrNull()?.roundOffDecimal()?.toString()
                        .orEmpty()
                } Kg"
            )

            tvTotalPayment.value(
                value = "Rp. ${
                    rsData?.realCalculationPrice?.toString()
                }"
            )

        }

        // setup status
        val mStatus = rsData?.status.toString()
        val mStatusdDesc = rsData?.statusDesc.toString()
        binding.status.build(mStatus, mStatusdDesc)
        binding.status.fontSizeSp(20f)

        binding.includeDetailRs.ivMainImage.loadImageFromURL(
            requireContext(), rsData?.photoPath?.toString()
        )

        statusLiveData.postValue(mStatus)

        // setUpMarker Into Map
        var location = LatLng(-34.0, 151.0)
        val apiLat = rsData?.lat?.toDouble()
        val apiLong = rsData?.long?.toDouble()
        if (apiLat != null && apiLong != null) {
            location = LatLng(apiLat, apiLong)
        }
        viewModel.mapLatLngLv.value = location

        if (mData?.driverData != null) {
            val driverData = mData.driverData
            binding.includeDriverInfo.apply {
                this.labelTitle.text = "Informasi Driver"
                this.ivMainImage.loadImageFromURL(requireContext(), driverData.photo_path)
                this.labelUserName.build("Nama : ", driverData.name, showHint = false)
                this.labelUserEmail.build("Email : ", driverData.email, showHint = false)
                this.labelContact.build("Contact : ", driverData.contact, showHint = false)
            }
        } else {
            viewGone(binding.includeDriverInfo.containerContent)
            binding.includeDriverInfo.labelTitle.text = "Belum Ada Driver"
        }

        if (mData?.truckData != null) {
            val truckData = mData.truckData
            binding.includeTruckInfo.apply {
                labelTitle.text = "Informasi Truck Penjemput"
                ivMainImage.loadImageFromURL(requireContext(), truckData.imageFullPath)
                labelDesc1.build("Nama : ", truckData.name, showHint = false)
                labelDesc2.build("Nopol : ", truckData.nopol, showHint = false)
                labelDesc3.build(
                    "Bahan Bakar : ", truckData.fuelType, showHint = false,
                )
            }
        } else {
            viewGone(binding.includeTruckInfo.containerContent)
            binding.includeTruckInfo.labelTitle.text = "Belum Ada Truck"
        }

        if (mData?.staffData != null) {
            val staffData = mData.staffData
            binding.includeStaffInfo.apply {
                labelTitle.text = "Informasi Staff"
                ivMainImage.loadImageFromURL(requireContext(), staffData.photo_path)
                labelUserName.build("Nama : ", staffData.name, showHint = false)
                labelUserEmail.build("Email : ", staffData.email, showHint = false)
                labelContact.build(
                    "Contact : ", staffData.contact, showHint = true,
                    hint = "Klik Untuk Hubungi Driver"
                )
            }
        } else {
            viewGone(binding.includeStaffInfo.containerContent)
            binding.includeStaffInfo.labelTitle.text = "Belum Ada Staff"
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

                binding.razVerticalStepper.adapter.clearData()
                historyData.forEachIndexed { index, item ->
                    val type = RazAlertType.PRIMARY

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

    private fun goToFragmentChat() {
        val directions =
            DetailHistoryFragmentDirections.actionDetailHistoryFragmentToRsChatFragment(
                args.rsID
            )
        findNavController().navigate(directions)
    }

    private fun goToFragmentInvoice() {
        val directions =
            DetailHistoryFragmentDirections.actionDetailHistoryFragmentToInvoiceFragment(
                args.rsID
            )
        findNavController().navigate(directions)
    }

    private fun goToFragmentUpdate() {
        val directions = DetailHistoryFragmentDirections
            .actionDetailHistoryFragmentToUpdateRsFragment(
                args.rsID
            )
        findNavController().navigate(directions)
    }


    private fun goToFragmentRsScale(rsStatus: String) {
        val directions = DetailHistoryFragmentDirections
            .actionDetailHistoryFragmentToRsScaleFragment(
                args.rsID,
                rsStatus
            )
        findNavController().navigate(directions)
    }

    override fun initData() {
        viewModel.getDetail(args.rsID)
        viewModel.detailRsLD.observe(viewLifecycleOwner, detailObserver)
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
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.prev_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.isMapReady.postValue(true)
    }

    private fun setupStatusSpinner() {
        val spinnerList =
            arrayListOf<String>(
                "Pilih Status Baru",
                "Menunggu Diproses",
                "Diproses",
                "Dalam Penjemputan",
                "Proses Timbang",
                "Sukses"
            )
        val spin = binding.spinnerStatus
        val onSpinnerItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItemPosition = binding.spinnerStatus.selectedItemPosition
                if (selectedItemPosition == 0) {

                } else {
                    val currentStatus = binding.spinnerStatus.selectedItem.toString()
                    var statusCode = "0"
                    when (selectedItemPosition) {
                        1 -> {
                            statusCode = "3"
                        }
                        2 -> {
                            statusCode = "2"
                        }
                        3 -> {
                            statusCode = "4"
                        }
                        4 -> {
                            statusCode = "5"
                        }
                        5 -> {
                            statusCode = "1"
                        }
                    }
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_are_you_sure),
                        message =
                        "Anda Yakin Ingin Mengubah Status Menjadi $currentStatus?",
                        positiveAction = Pair("OK", {
                            viewModel.changeStatus(args.rsID, statusCode)
                        }),
                        negativeAction = Pair("Batal", {
                            binding.spinnerStatus.setSelection(0)
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        spin.onItemSelectedListener = onSpinnerItemSelected

        // Create the instance of ArrayAdapter
        // having the list of courses
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerList.toList()
        )

        // set simple layout resource file
        // for each item of spinner
        arrayAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.adapter = arrayAdapter
    }


}