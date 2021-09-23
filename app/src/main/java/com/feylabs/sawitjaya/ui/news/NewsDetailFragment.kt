package com.feylabs.sawitjaya.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentNewsDetailBinding
import com.feylabs.sawitjaya.utils.MyHelper
import com.feylabs.sawitjaya.utils.base.BaseFragment

class NewsDetailFragment : BaseFragment() {

    var _binding : FragmentNewsDetailBinding? = null
    val binding get() = _binding as FragmentNewsDetailBinding


    companion object{
        const val DET_NEWS_TITLE = "dsZXa"
        const val DET_NEWS_AUTHOR = "dsWWa"
        const val DET_NEWS_CONTENT = "dsaDWE"
        const val DET_NEWS_CREATED_AT = "dRWsa"
        const val DET_NEWS_PHOTO = "FQFdsa"
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_news_detail, container, false)
        _binding = FragmentNewsDetailBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = getArguments()?.getString(DET_NEWS_TITLE);
        val author =  getArguments()?.getString(DET_NEWS_AUTHOR);
        val content =  getArguments()?.getString(DET_NEWS_CONTENT);
        val photo =  getArguments()?.getString(DET_NEWS_PHOTO);
        val created_at =  getArguments()?.getString(DET_NEWS_CREATED_AT);

        binding.apply {
            tvAuthor.text= "Ditulis Oleh "+ author
            tvContent.text = MyHelper.renderHTML(content.toString())
            tvDate.text = created_at
            tvMain.text = title
            Glide.with(binding.root).load(photo).into(ivMainImage)
        }



    }


}