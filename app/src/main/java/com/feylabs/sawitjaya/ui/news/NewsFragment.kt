package com.feylabs.sawitjaya.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.FragmentNewsBinding
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.utils.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel


class NewsFragment : BaseFragment() {

    val viewModel: AuthViewModel by viewModel()
    val adapter by lazy { NewsCompactAdapter() }

    var _binding: FragmentNewsBinding? = null
    val binding get() = _binding as FragmentNewsBinding

    override fun initUI() {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = adapter

        adapter.setAdapterInterfacez(obj = object : NewsCompactAdapter.NewsItemInterface {
            override fun onclick(model: NewsEntity?) {
                findNavController().navigate(
                    R.id.newsDetailFragment,
                    bundleOf(
                        NewsDetailFragment.DET_NEWS_TITLE to model?.title,
                        NewsDetailFragment.DET_NEWS_AUTHOR to model?.author,
                        NewsDetailFragment.DET_NEWS_CONTENT to model?.content,
                        NewsDetailFragment.DET_NEWS_CREATED_AT to model?.created_at,
                        NewsDetailFragment.DET_NEWS_PHOTO to model?.photo,
                    )
                )
            }

        })
    }

    override fun initObserver() {
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    viewGone(binding.includeLoading.root)
                    val tempList = mutableListOf<NewsEntity>()
                    it.data?.forEachIndexed { index, news ->
                        tempList.add(
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
                    adapter.setWithNewData(tempList.toMutableList())
                    adapter.notifyDataSetChanged()

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
        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
            getNews()
        }
    }

    override fun initData() {
        getNews()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        _binding = FragmentNewsBinding.bind(view)
        return binding.root
    }

    private fun getNews() {
        viewModel.getNews()
    }


}