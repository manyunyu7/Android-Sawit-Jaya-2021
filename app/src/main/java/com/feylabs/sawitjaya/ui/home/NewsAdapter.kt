package com.feylabs.sawitjaya.ui.home

import android.os.Build
import android.text.Html
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.ItemNewsBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

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

        var binding: ItemNewsBinding = ItemNewsBinding.bind(itemView)

        fun onBInd(model: NewsEntity?) {
            binding.tvMain.text = model?.title

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvSecondary.text = model?.created_at
//            binding.tvSecondary.text =
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    (Html.fromHtml("${model?.content}", Html.FROM_HTML_MODE_COMPACT));
//                } else {
//                    (Html.fromHtml("${model?.content}"));
//                }

            Glide.with(binding.root)
                .load(model?.photo)
                .into(binding.ivMainImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
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