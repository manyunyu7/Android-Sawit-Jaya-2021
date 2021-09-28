package com.feylabs.sawitjaya.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.ItemNewsBinding
import com.feylabs.sawitjaya.databinding.ItemNewsCompactBinding
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.UIHelper.renderHtmlToString

class NewsCompactAdapter : RecyclerView.Adapter<NewsCompactAdapter.NewsViewHolder>() {

    val data = mutableListOf<NewsEntity?>()
    lateinit var adapterInterface: NewsItemInterface

    fun setWithNewData(data: MutableList<NewsEntity?>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setAdapterInterfacez(obj: NewsItemInterface) {
        this.adapterInterface = obj
    }

    inner class NewsViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemNewsCompactBinding = ItemNewsCompactBinding.bind(itemView)

        fun onBInd(model: NewsEntity?) {
            binding.tvMain.text = model?.title

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvSecondary.text = model?.created_at

            binding.tvDescription.text = model?.content?.renderHtmlToString()

            binding.ivMainImage.loadImageFromURL(binding.root.context,model?.photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_compact, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface NewsItemInterface {
        fun onclick(model: NewsEntity?)
    }
}