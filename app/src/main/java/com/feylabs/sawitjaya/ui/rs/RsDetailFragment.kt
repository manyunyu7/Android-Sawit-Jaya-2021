package com.feylabs.sawitjaya.ui.rs

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.RsDetailFragmentBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.rs.adapter.RsPhotoAdapter
import com.feylabs.sawitjaya.ui.rs.model.RsModel
import com.feylabs.sawitjaya.ui.rs.model.RsPhotoModel
import com.feylabs.sawitjaya.utils.TelegramGalleryActivity
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

class RsDetailFragment : BaseFragment() {


    var _binding: RsDetailFragmentBinding? = null
    val binding get() = _binding as RsDetailFragmentBinding

    lateinit var authVIewModel: AuthViewModel

    var newestMargin: Double? = 0.0 ?: 0.0
    var newestPrice: Double? = 0.0 ?: 0.0
    var etEst = 0

    private val mAdapter by lazy { RsPhotoAdapter() }

    companion object {
        fun newInstance() = RsDetailFragment()
    }

    private lateinit var viewModel: RsDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.rs_detail_fragment, container, false)
        _binding = RsDetailFragmentBinding.bind(view)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RsDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCameraPermission()

        setupRecyclerView()


        val factory = ServiceLocator.provideFactory(requireContext())
        authVIewModel = ViewModelProvider(requireActivity(), factory).get(AuthViewModel::class.java)

        val model = arguments?.get("rs") as RsModel

        authVIewModel.getProfileLocally()
        authVIewModel.getPriceLocally()

        authVIewModel.priceLocalLiveData.observe(requireActivity(), Observer {

            if (it.size > 0) {
                if (it[0] != null) {
                    newestPrice = it[0]?.price
                    newestMargin = (it[0]?.margin)?.times(100)
                }
            }

        })

        binding.labelAddress.text = model.address
        binding.etEst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                countSimul()
            }

        })


        authVIewModel.localProfileLD.observe(requireActivity(), Observer {
            if (it != null) {
                binding.includeInfoUser.apply {
                    labelName.text = it.name
                    labelContact.text = it.contact
                    labelEmail.text = it.email

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //list of photos of seleced

        if (requestCode == 120) {

            val photos = data?.getSerializableExtra(GalleryActivity.PHOTOS) as List<String>
            if (photos != null) {

                //Clear adapter data

                mAdapter.clearData()
                val tempList = mutableListOf<RsPhotoModel>()
                photos.forEachIndexed { index, s ->
                    Timber.d("photo loop @${index}")
                    Timber.d("photo loop @${photos[index]}")
                    val uriFIle = "file://" + photos[index]
                    val mFileUri = Uri.parse(uriFIle) //for glide
                    val uploadedFile = File(Uri.parse(uriFIle).path.toString())
                    tempList.add(RsPhotoModel(mFileUri, uploadedFile))
                }
                mAdapter.setWithNewData(tempList)
                mAdapter.notifyDataSetChanged()
                showToast(mAdapter.data.size.toString())
            }


//            uploadedFile = File(Uri.parse(uriFIle).path.toString())

        }

    }

}