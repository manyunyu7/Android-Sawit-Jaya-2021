package com.feylabs.sawitjaya.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.FragmentUserHomeBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.utils.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_AUTHOR
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_CONTENT
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_CREATED_AT
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_PHOTO
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_TITLE
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import timber.log.Timber

class UserHomeFragment : BaseFragment() {


    var _binding: FragmentUserHomeBinding? = null
    val binding get() = _binding as FragmentUserHomeBinding

    lateinit var authViewModel: AuthViewModel

    var priceDataSetList = mutableListOf<DataPoint>()

    val adapterNews by lazy { NewsAdapter() }

    override fun initUI() {
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
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


        setNewsAdapter()
        setNewsRecylerView()

        binding.btnRequestSell.setOnClickListener {
            findNavController().navigate(R.id.rsPickLocationFragment)
        }


        val factory = ServiceLocator.provideFactory(requireContext())
        authViewModel = ViewModelProvider(requireActivity(), factory).get(AuthViewModel::class.java)
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        authViewModel.getProfileLocally()
        authViewModel.localProfileLD.observe(requireActivity(), Observer {
            updateView(it)
        })

        getNews()
        authViewModel.newsLocalLiveData.observe(requireActivity(), Observer {
            refreshNewsAdapter(it)
        })

        authViewModel.priceLocalLiveData.observe(requireActivity(), Observer {
            if (it.size > 0) {
                val newestPrice = it[0]?.price.toString()
                val newestMargin = (it[0]?.margin)?.times(100)
                binding.tvPriceToday.text = "Rp. $newestPrice"
                binding.tvMargin.text =
                    "Margin : ${newestMargin}% dari total harga jual tandan buah segar"

                val tempList = mutableListOf<DataPoint>()
                tempList.clear()
                var maxIndex = it.size
                it.forEachIndexed { index, priceResponseEntity ->
                    val price = (priceResponseEntity?.price)?.div(1000)?.toFloat()
                    Timber.d("price chared $price")
                    tempList.add(DataPoint(maxIndex.toFloat(), price!!))
                    maxIndex--
                }


                Timber.d("added set : ${tempList.asReversed()}")
                val dataset = Dataset(
                    tempList.asReversed()
                )


                binding.liveChart.setDataset(dataset)
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
                    // Draw last point tag label
                    .drawLastPointLabel()
                    .drawDataset()
            } else {
                binding.tvPriceToday.text = "Loading...."
            }
        })

        authViewModel.getPrices(true)

    }

    fun drawChart(dataSet: Dataset) {
        // set dataset, display options, and ... draw!
        binding.liveChart.setDataset(dataSet)
            .drawYBounds()
            .drawBaseline()
            .drawFill()
            .drawSmoothPath()
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
            try{
                binding.ivProfileTopLeft.loadImageFromURL(
                    requireContext(),prof?.photo
                )
            }catch (e:Exception){

            }

        }
    }

    private fun getNews() {
        authViewModel.getNews()
        authViewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
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
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })
    }

}