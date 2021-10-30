package com.feylabs.sawitjaya.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.FragmentUserHomeBinding
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_AUTHOR
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_CONTENT
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_CREATED_AT
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_PHOTO
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_TITLE
import com.feylabs.sawitjaya.utils.MyHelper.roundOffDecimal
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.utils.DialogUtils
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserHomeFragment : BaseFragment() {

    var _binding: FragmentUserHomeBinding? = null
    val binding get() = _binding as FragmentUserHomeBinding

    val authViewModel: AuthViewModel by viewModel()

    val adapterNews by lazy { NewsAdapter() }

    val MULTIPLE_PERMISSIONS = 99 // code you want.

    var permissions = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun initUI() {
        setNewsAdapter()
        setNewsRecylerView()
    }

    override fun initObserver() {
        binding.tvPriceToday.text = "Loading...."
        binding.tvMargin.text = "Loading..."
        binding.tvNotifTitle.text = "Loading..."
        binding.tvNotifContent.text = "Loading..."

        authViewModel.landingMessageLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.cardNotif.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.cardNotif.visibility = View.GONE
                }
                is Resource.Success->{
                    val data = it.data
                    if (data != null) {
                        binding.cardNotif.visibility = View.VISIBLE
                        binding.tvNotifTitle.text = data.title
                        binding.tvNotifContent.text = data.contentMessage
                    } else {
                        binding.cardNotif.visibility = View.GONE
                    }
                }
                else -> {}
            }
        })

        authViewModel.newsLocalLiveData.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                refreshNewsAdapter(it)
            }
        })

        authViewModel.pricesLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response is Resource.Success) {
                val data = response.data
                data?.let {
                    if (it.isNotEmpty()) {
                        val newestPrice = it[0]?.price.toString()
                        val newestMargin =
                            (it[0]?.margin)?.toDouble()?.times(100)?.roundOffDecimal()
                        binding.tvPriceToday.text = "Rp. $newestPrice"
                        binding.tvMargin.text =
                            "Margin : ${newestMargin}% dari total harga jual tandan buah segar"

                        val tempList = mutableListOf<DataPoint>()
                        tempList.clear()
                        var maxIndex = it.size

                        it.forEachIndexed { index, priceResponseEntity ->
                            val price = (priceResponseEntity?.price)?.toFloat()
                            Timber.d("price chared $price")
                            tempList.add(DataPoint(maxIndex.toFloat(), price!!))
                            maxIndex--
                        }

                        Timber.d("added set : ${tempList.asReversed()}")
                        val dataset = Dataset(
                            tempList.asReversed()
                        )
                        drawChart(dataset)
                    } else {
                        binding.tvPriceToday.text = "Loading...."
                    }
                }
            }
        })


        authViewModel.userLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    authViewModel.getProfileLocally()
                }
            }
        })

        authViewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    authViewModel.clearNews()
                    it.data?.forEachIndexed { index, news ->
                        authViewModel.saveNews(
                            NewsEntity(
                                id = news.id,
                                title = news.title,
                                author = news.author,
                                content = news.content,
                                photo = news.photo_path,
                                created_at = news.createdAt,
                                updated_at = news.updatedAt,
                            )
                        )
                    }
                    // activity level
                    authViewModel.getNewsLocally()
                }
            }
        })
    }

    override fun initAction() {

        binding.btnSellSawit.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                DialogUtils.showCustomDialog(
                    context = requireContext(),
                    title = getString(R.string.title_modal_attention),
                    message = "Izin Akses Lokasi Belum Diizinkan, Silakan Izinkan untuk melanjutkan menggunakan aplikasi",
                    positiveAction = Pair("Izinkan GPS", {
                        checkPermissions()
                    }),
                    negativeAction = Pair(getString(R.string.dialog_cancel), {

                    }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            } else {
                val manager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager;

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_attention),
                        message = "Untuk Menggunakan Menu Ini, Anda Harus Mengaktifkan GPS Anda, \n\n Silakan Aktifkan GPS dan Coba Lagi",
                        positiveAction = Pair(getString(R.string.title_activate_gps), {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }),
                        negativeAction = Pair(getString(R.string.dialog_cancel), {

                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                } else {
                    findNavController().navigate(R.id.rsPickLocationFragment)
                }
            }

        }

        binding.btnSendSawit.setOnClickListener {
            DialogUtils.showCustomDialog(
                context = requireContext(),
                title = getString(R.string.title_modal_attention),
                message = "Saat Ini Fitur Tersebut Belum Tersedia atau Dalam tahap pengembangan",
                positiveAction = Pair(getString(R.string.dialog_ok), { }),
                negativeAction = Pair(getString(R.string.dialog_cancel), { }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        binding.btnRequestSell.setOnClickListener {
            findNavController().navigate(R.id.rsPickLocationFragment)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }

    override fun initData() {
        authViewModel.getLandingMessage()
        authViewModel.getPrices(true)

        uiScope.launch(Dispatchers.IO) {
            authViewModel.getProfileLocally()
            authViewModel.getNewsLocally()
        }
        authViewModel.getProfileByUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        getNews()
        authViewModel.getPrices(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)
        _binding = FragmentUserHomeBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.localProfileLD.observe(requireActivity(), Observer {
            updateView(it)
        })

        authViewModel.getPrices(true)

    }

    private fun drawChart(dataset: Dataset) {
        // set dataset, display options, and ... draw!
        binding.liveChart.setDataset(dataset)
            // Draws the Y Axis bounds with Text data points.
            .drawYBounds()
            // Draws a customizable base line from the first point of the dataset or manually set a data point
            .drawBaseline()
            // Set manually the data point from where the baseline draws,
            .setBaselineManually(1.5f)
            // Draws a fill on the chart line. You can set whether to draw with a transparent gradient
            // or a solid fill. Defaults to gradient.
            .drawFill(withGradient = true)
            // draws the color of the path and fill conditional to being above/below the baseline datapoint
            .drawBaselineConditionalColor()
            // Draw Guidelines in the background
            .drawVerticalGuidelines(steps = 4)
            .drawHorizontalGuidelines(steps = 4)
            // Draw smooth path
            .drawSmoothPath()
            .drawTouchOverlayAlways()
            // Draw last point tag label
            .drawLastPointLabel()
            .drawDataset()
    }


    private fun refreshNewsAdapter(list: List<NewsEntity?>) {
        adapterNews.setWithNewData(list.toMutableList())
        adapterNews.notifyDataSetChanged()
    }

    private fun setNewsAdapter() {
        adapterNews.adapterInterface = object : NewsAdapter.NewsItemInterface {

            override fun onclick(model: NewsEntity?) {
                UIHelper.showLongToast(requireContext(), model?.title.toString())
                findNavController().navigate(
                    R.id.action_userHomeFragment_to_newsDetailFragment,
                    bundleOf(
                        DET_NEWS_TITLE to model?.title,
                        DET_NEWS_AUTHOR to model?.author,
                        DET_NEWS_CONTENT to model?.content,
                        DET_NEWS_CREATED_AT to model?.created_at,
                        DET_NEWS_PHOTO to model?.photo,
                    )
                )
            }

        }

    }

    private fun setNewsRecylerView() {
        binding.rvNews.adapter = adapterNews
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateView(prof: AuthEntity?) {
        binding.apply {
            binding.greetingName.text = prof?.name
            try {
                binding.ivProfileTopLeft.loadImageFromURL(
                    requireContext(), prof?.photo
                )
            } catch (e: Exception) {

            }

        }
    }

    private fun getNews() {
        authViewModel.getNews()
    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(requireActivity(), p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


}