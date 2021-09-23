package com.feylabs.sawitjaya.ui.user_history_tbs.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.remote.response.HistoryDetailResponse
import com.feylabs.sawitjaya.databinding.FragmentDetailHistoryBinding
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel


class DetailHistoryFragment : BaseFragment() {

    val viewModel: DetailHistoryViewModel by viewModel()

    val photoAdapter by lazy { DetailHistoryPhotoAdapter() }

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
                    val mData = it.data
                    binding.includeInfoUser.apply {
                        val userData = mData?.userData
                        imageView.loadImageFromURL(
                            requireContext(),
                            mData?.data?.userPhoto.toString()
                        )
                        this.labelName.text = userData?.name
                        this.labelEmail.text = userData?.email
                        this.labelContact.text = userData?.contact

                        mData?.data?.mapToPhotoModel()?.let { listPhoto ->
                            photoAdapter.setWithNewData(
                                listPhoto
                            )
                        }
                    }

                    val color = UIHelper.setColorStatus(mData?.data?.status.toString())
                    binding.etStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    binding.containerStatus.setCardBackgroundColor(ContextCompat.getColor(requireContext(), color))
                    binding.etStatus.text = mData?.data?.statusDesc.toString()
                    binding.labelAddress.text = mData?.data?.address
                    binding.labelSimulasi.text = mData?.data?.address
                }
                is Resource.Error -> {
                    viewGone(binding.includeLoading.root)
                }

                is Resource.Loading->{
                    viewVisible(binding.includeLoading.root)
                }

                else -> {
                }//Do Nothing
            }
        }
    }

    override fun initAction() {
        photoAdapter.setAdapterInterfacez(object : DetailHistoryPhotoAdapter.RsPhotoItemInterface {
            override fun onclick(model: PhotoListModel?) {
                requireActivity().actionBar?.hide()
                (activity as AppCompatActivity?)?.supportActionBar?.hide()
                viewVisible(binding.includePreview.root)
                Glide.with(requireContext()).load(model?.url)
                    .into(binding.includePreview.fullscreenContent)
                binding.includePreview.btnBack.setOnClickListener {
                    requireActivity().actionBar?.show()
                    (activity as AppCompatActivity?)?.supportActionBar?.show()
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