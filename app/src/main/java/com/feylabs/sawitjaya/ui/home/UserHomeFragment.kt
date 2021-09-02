package com.feylabs.sawitjaya.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.FragmentUserHomeBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_AUTHOR
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_CONTENT
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_CREATED_AT
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_PHOTO
import com.feylabs.sawitjaya.ui.news.NewsDetailFragment.Companion.DET_NEWS_TITLE
import com.feylabs.sawitjaya.utils.UIHelper
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset

class UserHomeFragment : Fragment() {


    var _binding: FragmentUserHomeBinding? = null
    val binding get() = _binding as FragmentUserHomeBinding

    lateinit var authViewModel: AuthViewModel

    val adapterNews by lazy { NewsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        getNews()
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

        val liveChart = binding.liveChart

        val dataset = Dataset(
            mutableListOf(
                DataPoint(0f, 1f),
                DataPoint(1f, 3f),
                DataPoint(2f, 6f),
                DataPoint(3f, 36f),
                DataPoint(4f, 61f),
                DataPoint(5f, 43f),
                DataPoint(6f, 70f),
                DataPoint(7f, 30f),
                DataPoint(8f, 10f),
            )
        )

        // set dataset, display options, and ... draw!
        liveChart.setDataset(dataset)
            .drawYBounds()
            .drawBaseline()
            .drawFill()
            .drawSmoothPath()
            .drawDataset()

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
            val showedText =
                Glide.with(requireActivity())
                    .load(prof?.photo)
                    .into(binding.ivProfileTopLeft)
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