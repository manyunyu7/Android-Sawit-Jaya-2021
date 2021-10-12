package com.feylabs.sawitjaya.ui.rs.request

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.remote.request.RequestSellRequest
import com.feylabs.sawitjaya.databinding.FragmentRsDetailBinding
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.rs.request.adapter.RsPhotoAdapter
import com.feylabs.sawitjaya.ui.rs.request.model.RsModel
import com.feylabs.sawitjaya.ui.rs.request.model.RsPhotoModel
import com.feylabs.sawitjaya.utils.DialogUtils
import com.feylabs.sawitjaya.utils.TelegramGalleryActivity
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

class RsDetailFragment : BaseFragment() {


    var _binding: FragmentRsDetailBinding? = null
    val binding get() = _binding as FragmentRsDetailBinding

    val authViewModel: AuthViewModel by viewModel()
    val viewModel: RsDetailViewModel by viewModel()

    var newestMargin: Double? = 0.0 ?: 0.0
    var newestPrice: Double? = 0.0 ?: 0.0

    var tempFileListGlobal = mutableListOf<File?>()

    lateinit var rsModel: RsModel

    private val mAdapter by lazy { RsPhotoAdapter() }

    //Observer for upload rs progress
    lateinit var uploadRSObserver: Observer<Resource<String?>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_rs_detail, container, false)
        _binding = FragmentRsDetailBinding.bind(view)
        return binding.root
    }


    private fun uploadRSDataToServer() {
        val sEstWeight = binding.etEst.text.toString()
        val sAddress = binding.labelAddress.text.toString()
        val sLat = rsModel.lat.toString()
        val sLong = rsModel.long.toString()
        val sContact = binding.etContact.text.toString()
        viewModel.uploadData(
            RequestSellRequest(
                additionalContact = sContact,
                address = sAddress,
                contact = sContact,
                lat = sLat,
                long = sLong,
                status = "3",
                uploadFile = tempFileListGlobal,
                estWeight = sEstWeight
            )
        ).observe(viewLifecycleOwner, uploadRSObserver)
    }

    private fun countSimul() {
        var finalResult = "Masukan estimasi berat untuk menghitung estimasi"
        try {
            val weight = binding.etEst.text.toString().toDouble()
            val localeID = Locale("in", "ID")
            val formatRupiah =
                NumberFormat.getCurrencyInstance(localeID)
            val weightBruto = weight
            val weightMargin = weight * (newestMargin?.div(100.0)!!)
            Timber.d("weight margin ${weightMargin}")
            val weightNetto = weightBruto - (weightMargin)
//            val weightNetto = 5.495 - weightMargin
            // hasil 5220
            Timber.d("weight netto ${weightNetto}")


            val finalPrice = weightNetto?.times(newestPrice!!)
//                val simulValue = (weight.toDouble() - (newestMargin?.times(weight.toDouble())!!)) * newestPrice!!
            val result = formatRupiah.format(finalPrice)

            finalResult =
                "Berat Kotor : ${weightBruto} \n" +
                        "Berat Bersih : ${weightNetto} \n" +
                        "Margin  : ${newestMargin} \n" +
                        "Harga Sawit : ${newestPrice}\n" +
                        "Estimasi Harga Jual Sawit anda adalah $result, hasil ini dapat berubah sesuai kondisi ketika penjemputan"
            binding.labelSimulasi.text = finalResult

        } catch (e: Exception) {
            // DO Nothing
        }
    }

    private fun setupRecyclerView() {

        mAdapter.setWithNewData(
            mutableListOf(
                RsPhotoModel(null, null),
                RsPhotoModel(null, null),
                RsPhotoModel(null, null),
                RsPhotoModel(null, null),
                RsPhotoModel(null, null),
                RsPhotoModel(null, null),
            )
        )

        mAdapter.setAdapterInterfacez(object : RsPhotoAdapter.RsPhotoItemInterface {
            override fun onclick(model: RsPhotoModel?) {
                pickPhoto()
            }
        })

        binding.rvPhotos.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhotos.setHasFixedSize(true)
        binding.rvPhotos.adapter = mAdapter
    }

    private fun pickPhoto() {
        Timber.d("open album")
        val config = GalleryConfig.Build()
            .limitPickPhoto(5)
            .singlePhoto(false)
            .build()

        TelegramGalleryActivity.openActivity(this, 120, config)

    }

    private fun checkCameraPermission() {

    }

    override fun initUI() {
        //get model data from arguments ( parcelable )
        val model = arguments?.get("rs") as RsModel
        rsModel = model
        binding.labelAddress.text = model.address

        checkCameraPermission()
        setupRecyclerView()
    }

    override fun initObserver() {
        authViewModel.priceLocalLiveData.observe(requireActivity(), Observer {

            if (it.isNotEmpty()) {
                if (it[0] != null) {
                    newestPrice = it[0]?.price
                    newestMargin = (it[0]?.margin)?.times(100)
                }
            }
        })

        authViewModel.localProfileLD.observe(requireActivity(), Observer {
            if (it != null) {
                binding.includeInfoUser.apply {
                    labelName.text = it.name
                    labelContact.text = it.contact

                    try {
                        Glide.with(requireContext()).load(it.photo)
                            .placeholder(R.drawable.ic_placeholder)
                            .centerCrop()
                            .into(imageView)
                    } catch (e: Exception) {

                    }
                }
            }
        })

        uploadRSObserver = Observer {
            when (it) {
                is Resource.Success -> {
                    viewGone(binding.includeLoading.root)
                    binding.tvUploadProgress.visibility = View.GONE
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.message_modal_success_req_rs),
                        positiveAction =
                        Pair(getString(R.string.label_Ok),
                            {
                                findNavController().navigate(R.id.userHomeFragment)
                            }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                    binding.tvUploadProgress.visibility = View.GONE
                    showToast(it.data.toString())
                }
                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                    binding.tvUploadProgress.visibility = View.VISIBLE
                    binding.tvUploadProgress.text = it.data
                }
            }
        }

    }

    override fun initAction() {
        // On Request Sell submit button click
        binding.btnSubmit.setOnClickListener {
            DialogUtils.showCustomDialog(
                context = requireContext(),
                title = getString(R.string.title_modal_are_you_sure),
                message = getString(R.string.message_modal_rs_sell_confirmation),
                positiveAction = Pair("Ya", {
                    uploadRSDataToServer()
                }),
                negativeAction = Pair("Tidak", {
                }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }


        binding.etEst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                countSimul()
            }

        })

    }

    override fun initData() {
        authViewModel.getPrices(true)
        authViewModel.getProfileLocally()
        authViewModel.getPriceLocally()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //list of photos of seleced
        if (requestCode == 120) {
            val photos = data?.getSerializableExtra(GalleryActivity.PHOTOS) as List<String>
            if (photos != null) {
                //Clear adapter data
                mAdapter.clearData()
                tempFileListGlobal.clear()
                val tempList = mutableListOf<RsPhotoModel>()
                photos.forEachIndexed { index, s ->
                    Timber.d("photo loop @${index}")
                    Timber.d("photo loop @${photos[index]}")
                    val uriFIle = "file://" + photos[index]
                    val mFileUri = Uri.parse(uriFIle) //for glide
                    val uploadedFile = File(Uri.parse(uriFIle).path.toString())
                    tempList.add(RsPhotoModel(mFileUri, uploadedFile))
                    tempFileListGlobal.add(uploadedFile)
                }
                mAdapter.setWithNewData(tempList)
                mAdapter.notifyDataSetChanged()
                showToast(mAdapter.data.size.toString())
            }
        }

    }

}