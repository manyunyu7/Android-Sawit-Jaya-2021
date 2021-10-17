package com.feylabs.sawitjaya.ui.rs_signature

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.databinding.FragmentRsSignatureBinding
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.utils.DialogUtils
import com.feylabs.sawitjaya.utils.MyHelper.roundOffDecimal
import com.feylabs.sawitjaya.utils.MyHelper.toDoubleStringRoundOff
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.github.gcacace.signaturepad.views.SignaturePad
import org.koin.android.viewmodel.ext.android.viewModel
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType

import smartdevelop.ir.eram.showcaseviewlib.GuideView
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class RsSignatureFragment : BaseFragment() {


    val viewModel: RsSignatureViewModel by viewModel()

    private var _binding: FragmentRsSignatureBinding? = null
    private val binding get() = _binding as FragmentRsSignatureBinding

    private val args: RsSignatureFragmentArgs by navArgs()

    lateinit var uploadObserver: Observer<Resource<String?>>

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initUI() {
        setupShowCase()
        setupStatusSpinner()
    }

    private fun setupShowCase() {
        showCaseHelper("Petunjuk", "Pilih Pemilik Tanda Tangan", binding.spinnerStatus) {
            showCaseHelper(
                "Petunjuk",
                "Gambar Tanda Tangan Disini",
                binding.containerSignature
            ) {
                showCaseHelper("Petunjuk", "Klik Untuk Hapus Tanda Tangan", binding.btnClear) {
                    showCaseHelper(
                        "Petunjuk",
                        "Klik Untuk Menyimpan Tanda Tangan Persetujuan Penjualan",
                        binding.btnSaveSignature
                    ) {}
                }
            }
        }

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
//                        binding.tvTotalWeight.value("Belum Ada Data")
                        binding.includeInvoiceDet.tvDetailWeight.value("Belum Ada Data Timbangan")
                    } else {
//                        binding.tvTotalWeight.value(detailWeight)
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

        viewModel.storeFinalLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.message_modal_success_finish_transaction),
                        positiveAction =
                        Pair(getString(R.string.label_Ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewGone(binding.includeLoading.root)
                }
                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                }

                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }
            }
        })

        uploadObserver = Observer {
            when (it) {
                is Resource.Success -> {
                    viewModel.getDetail(args.rsID)
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.modal_message_success_save_signature),
                        positiveAction =
                        Pair(getString(R.string.label_Ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    binding.signaturePad.clearView()
                    binding.signaturePad.clear()
                    viewGone(binding.includeLoading.root)
                }
                is Resource.Error -> {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_error_occured),
                        message = getString(R.string.message_modal_error_save_signature),
                        positiveAction = Pair("OK", {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewGone(binding.includeLoading.root)
                }
                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }
            }
        }
    }

    private fun setupDetailUI(it: Resource.Success<HistoryDetailResponse?>) {
        val mData = it.data
        val userData = mData?.userData
        val rsData = mData?.data
        val priceData = mData?.price

        setupBtnSaveFinalData(it)

        binding.tvCoreFinalPrice.build("Harga Akhir : ", "${rsData?.resultEstPriceNow}")
        binding.etAdjustment.setText(rsData?.resultEstPriceNow)
        binding.tvCoreCurrentPrice.value(priceData?.price?.toDoubleStringRoundOff().toString())
        binding.tvCoreCurrentMargin.value(priceData?.margin?.toString().toString())

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

                binding.tvCoreTotalWeight.value(
                    mData?.totalWeight?.toDoubleStringRoundOff().toString()
                )

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
                    value = priceData?.margin?.times(100)?.toString().toString()
                )

                tvOldPrice.value(
                    value = "Rp. ${rsData?.estPrice.toString()}"
                )
                tvFinalPrice.value(
                    value = "Rp. ${priceData?.price.toString()}"
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
                        rsData?.realCalculationPrice?.toString()
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

    private fun setupBtnSaveFinalData(it: Resource.Success<HistoryDetailResponse?>) {
        val mData = it.data
        val rsData = mData?.data

        binding.btnSaveFinalData.isEnabled = true
        binding.btnSaveFinalData.setOnClickListener {
            var isOKE = true
            var currentMessage = "Tidak Dapat Mengupload Invoice\n\n"

            if (rsData?.signature_user_path == "") {
                isOKE = false
                currentMessage += "Pemilik Kebun"
            }

            if (rsData?.signature_staff_path == "") {
                isOKE = false
                currentMessage += ", Staff"
            }


            if (rsData?.signature_driver_path == "") {
                isOKE = false
                currentMessage += " dan Driver"
            }

            currentMessage += "\n\nBelum menandatangani invoice, tanda tangan sudah harus diisi sebelum mengupload invoice"


            if (isOKE) {
                DialogUtils.showCustomDialog(
                    context = requireContext(),
                    title = getString(R.string.title_modal_are_you_sure),
                    message = getString(R.string.message_modal_upload_invoice_confirmation),
                    positiveAction = Pair("OK", {
                        uploadInvoice()
                    }),
                    negativeAction = Pair("BATAL", {}),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            } else {
                DialogUtils.showCustomDialog(
                    context = requireContext(),
                    title = getString(R.string.title_modal_attention),
                    message = currentMessage,
                    positiveAction = Pair("OK", {}),
                    negativeAction = Pair("BATAL", {}),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }

        }

    }


    fun storeSignature() {
        val file =
            File(getRealPathFromURI(bitmapToFile(binding.signaturePad.signatureBitmap)).toString())

        var type = "0"
        var isError = false
        when (binding.spinnerStatus.selectedItemPosition) {
            1 -> {  // Pemilik Kebun
                type = "1"
            }
            2 -> {  //Staff
                type = "3"
            }
            3 -> { //Driver
                type = "2"
            }
            else -> {
                isError = true
            }
        }
        if (!isError)
            viewModel.upload(args.rsID, type, file).observe(viewLifecycleOwner, uploadObserver)
        else
            showToast("Tidak Valid")

    }

    override fun initAction() {

        binding.btnSaveFinalData.isEnabled = false


        binding.btnSaveSignature.setOnClickListener {

            if (binding.spinnerStatus.selectedItemPosition == 0) {
                DialogUtils.showCustomDialog(
                    context = requireContext(),
                    title = getString(R.string.title_modal_attention),
                    message =
                    getString(R.string.message_modal_choose_signature_owner),
                    positiveAction = Pair("OK", {}),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            } else {
                if (binding.signaturePad.isEmpty) {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_attention),
                        message =
                        getString(R.string.message_modal_error_empty_signature),
                        positiveAction = Pair("OK", {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                } else {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_are_you_sure),
                        message =
                        getString(R.string.message_modal_save_user_signature),
                        positiveAction = Pair("OK", {
                            storeSignature()
                        }),
                        negativeAction = Pair("Batal", {
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }


        }

        val mSignaturePad = binding.signaturePad
        mSignaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {

            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })

        binding.btnClear.setOnClickListener {
            mSignaturePad.clear()
            mSignaturePad.clearView()
        }

        binding.btnSeeData.setOnClickListener {
            goToFragmentDetail()
        }

    }

    private fun uploadInvoice() {
        showToast("Mengupload Invoice")
        val finalPrice = binding.tvCoreCurrentPrice.value
        val finalMargin = binding.tvCoreCurrentMargin.value
        val pricePaid = binding.etAdjustment.text.toString()

        viewModel.storeFinalData(
            id = args.rsID,
            finalPrice = finalPrice,
            finalMargin = finalMargin,
            pricePaid = pricePaid
        )
    }

    override fun initData() {
        viewModel.fetchScaleData(args.rsID)
        viewModel.getDetail(args.rsID)
    }

    fun goToFragmentDetail() {
        val directions =
            RsSignatureFragmentDirections.actionRsSignatureFragmentToUpdateRsFragment(args.rsID)
        findNavController().navigate(directions)
    }


    private fun showCaseHelper(
        title: String,
        content: String,
        view: View,
        onDismiss: (() -> Unit)
    ) {
        view.requestFocus()
        GuideView.Builder(requireContext())
            .setTitle(title)
            .setContentText(content)
            .setTargetView(view)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setGuideListener { onDismiss.invoke() }
            .setDismissType(DismissType.outside) //optional - default dismissible by TargetView
            .build()
            .show()
    }

    private fun setupStatusSpinner() {
        val spinnerList =
            arrayListOf<String>(
                "Pilih Pemilik Tanda Tangan",
                "Pemilik Kebun", // 1
                "Staff",         // 3
                "Driver"         // 2
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRsSignatureBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(requireContext())
        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Error Bitmap ", e.toString())
        }
        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor = requireActivity().contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }


}