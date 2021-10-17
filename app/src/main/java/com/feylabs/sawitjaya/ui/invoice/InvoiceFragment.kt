package com.feylabs.sawitjaya.ui.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.databinding.FragmentInvoiceBinding
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.utils.MyHelper.roundOffDecimal
import com.feylabs.sawitjaya.utils.MyHelper.toDoubleStringRoundOff
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class InvoiceFragment : BaseFragment() {

    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding as FragmentInvoiceBinding

    val viewModel: InvoiceViewModel by viewModel()
    private val args: InvoiceFragmentArgs by navArgs()

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.scaleLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }
                is Resource.Success -> {
                    viewGone(binding.includeLoading.root)
                    var detailWeight = ""
                    it.data?.resData.let { listData ->
                        // add data to recyclerview
                        listData?.data?.forEachIndexed { index, data ->
                            Timber.d("nry weighted $data")
                            detailWeight += "${index + 1}. ${data.result} Kg Ditimbang Oleh : (${data.staff_name})\n"
                        }
                    }

                    if (detailWeight == "") {
                        binding.includeInvoiceDet.tvDetailWeight.value("Belum Ada Data Timbangan")
                    } else {
                        binding.includeInvoiceDet.tvDetailWeight.value(detailWeight)
                    }
                }
                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                    showToast("Terjadi Kesalahan")
                }
            }
        })

        viewModel.detailRsLD.observe(viewLifecycleOwner, Observer {
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
        })
    }

    override fun initAction() {
        binding.btnSignature.setOnClickListener {
            goToFragmentSignature()
        }
    }

    fun goToFragmentSignature() {
        val directions =
            InvoiceFragmentDirections.actionInvoiceFragmentToRsSignatureFragment(args.rsID)
        findNavController().navigate(directions)
    }

    override fun initData() {
        viewModel.getDetail(args.rsID)
        viewModel.fetchScaleData(args.rsID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInvoiceBinding.inflate(inflater)
        return binding.root
    }

    private fun setupDetailUI(it: Resource.Success<HistoryDetailResponse?>) {
        val mData = it.data
        val userData = mData?.userData
        val rsData = mData?.data
        val priceData = mData?.price

        binding.includeInvoiceDet.apply {

            tvTransactionNumber.title(rsData?.rsCode.toString())

            userData.let {
                tvUserContact.fontSizeSp(16f)
                tvUserEmail.fontSizeSp(16f)
                tvUserContact.build(
                    "Kontak User : ",
                    it?.contact.toString()
                )
                tvUserEmail.build(
                    "Email : ",
                    it?.email.toString()
                )

                binding.includeSignature.apply {

                    val driverData = mData?.driverData
                    val staffData = mData?.staffData


                    if (driverData?.name == null) {
                        viewGone(ivSignatureDriver)
                        labelDriverName.text = "Belum\n Ditandatangani Driver"
                    } else {
                        ivSignatureDriver.loadImageFromURL(
                            requireContext(),
                            rsData?.signature_driver_path
                        )
                        labelDriverName.text = "Driver\n${rsData?.driverName}"
                    }

                    if (rsData?.signature_user_path == null) {
                        viewGone(ivSignatureOwner)
                        labelOwnerName.text = "Belum\n Ditandatangani Pemilik"
                    } else {
                        ivSignatureOwner.loadImageFromURL(
                            requireContext(),
                            rsData?.signature_user_path
                        )
                        labelOwnerName.text = "Pemilik Kebun\n${rsData?.userName}"
                    }

                    if (staffData?.name == null) {
                        viewGone(ivSignatureStaff)
                        labelStaffName.text = "Belum\n Ditandatangani Staff"
                    } else {
                        ivSignatureStaff.loadImageFromURL(
                            requireContext(),
                            rsData?.signature_staff_path
                        )
                        Timber.d("nry staff_name ${staffData?.name}")
                        labelStaffName.text = "Staff\n${rsData?.staffName}"

                    }

                }


                tvDate.value(rsData?.createdAt.toString())
                tvEstWeight.value(
                    rsData?.estWeight?.toDoubleStringRoundOff().toString() + " Kg"
                )

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

                tvUserName.value(rsData?.userName.toString())
                tvDate.value(rsData?.createdAt.toString())

                tvAddress.build(
                    title = "Alamat Pengantaran",
                    value = rsData?.address.toString()
                )
                tvOldMargin.value(
                    value = rsData?.estMargin?.toDouble()?.times(100)?.roundOffDecimal().toString()
                )

                tvCurrentMargin.value(
                    value = rsData?.finalMargin.toString()
                )

                tvOldPrice.value(
                    value = "Rp. ${rsData?.estPrice.toString()}"
                )

                tvFinalPrice.value(
                    value = "Rp. ${rsData?.finalPrice.toString()}"
                )

                tvTotalWeight.value(
                    value = "${
                        mData?.totalWeight?.toString()?.toDoubleOrNull()?.roundOffDecimal()
                            ?.toString()
                            .orEmpty()
                    } Kg"
                )

                tvTotalPayment.value(
                    value = "Rp. ${
                        rsData?.pricePaid?.toString()
                    }"
                )

            }

            // setup status
            val mStatus = rsData?.status.toString()
            val mStatusdDesc = rsData?.statusDesc.toString()

            tvStatus.value(mStatusdDesc)

            if (mData?.driverData != null) {
                val driverData = mData.driverData
                binding.apply {
                    tvDriverName.build("Nama Driver : ", driverData.name.toString())
                    tvDriverContact.build("Kontak Driver : ", driverData.contact.toString())
                    tvDriverEmail.build("Email Driver : ", driverData.email.toString())
                }

            } else {
//            viewGone(binding.includeDriverInfo.containerContent)
//            binding.includeDriverInfo.labelTitle.text = "Belum Ada Driver"
            }

            if (mData?.truckData != null) {
                val truckData = mData.truckData
                binding.apply {
                    tvTruckName.build("Armada : ", truckData.name)
                    tvTruckNopol.build("Nomor Polisi : ", truckData.nopol)
                    tvTruckType.build("Jenis Truck : ", truckData.fuelType)
                }

            } else {
                // if truck data is empty
            }

            if (mData?.staffData != null) {
                val staffData = mData.staffData
                binding.apply {
                    tvStaffName.build("Nama Staff : ", staffData.name.toString())
                    tvStaffContact.build("Contact Staff : ", staffData.contact.toString())
                    tvStaffEmail.build("Email Staff : ", staffData.email.toString())
                }
            } else {
                // if staff data is empty
            }
        }
    }


}