package com.feylabs.sawitjaya.ui.mnotification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ItemMNotificationBinding
import com.feylabs.sawitjaya.databinding.ItemNewsCompactBinding
import com.feylabs.sawitjaya.utils.UIHelper.renderHtmlToString

class MNotificationAdapter : RecyclerView.Adapter<MNotificationAdapter.NewsViewHolder>() {

    val data = mutableListOf<MNotificationModel>()
    lateinit var adapterInterface: MNotificationInterface

    fun setWithNewData(data: MutableList<MNotificationModel>) {
        this.data.clear()
        this.data.addAll(data)
        this.notifyDataSetChanged()
    }

    fun setAdapterInterfacez(obj: MNotificationInterface) {
        this.adapterInterface = obj
    }

    inner class NewsViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemMNotificationBinding = ItemMNotificationBinding.bind(itemView)

        fun onbind(model: MNotificationModel?) {
            binding.tvMain.text = model?.title

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvSecondary.text = model?.createdAt?.renderHtmlToString()
            binding.tvDescription.text = model?.message?.renderHtmlToString()

            //TODO Ganti image dari server
            // binding.ivMainImage.loadImageFromURL(binding.root.context, model?.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_m_notification, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onbind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface MNotificationInterface {
        fun onclick(model: MNotificationModel?)
    }
}