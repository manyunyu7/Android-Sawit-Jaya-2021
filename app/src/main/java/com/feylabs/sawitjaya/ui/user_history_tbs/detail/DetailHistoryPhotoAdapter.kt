package com.feylabs.sawitjaya.ui.user_history_tbs.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ItemPhotoRsBinding
import com.feylabs.sawitjaya.ui.rs.request.model.RsPhotoModel
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL

class DetailHistoryPhotoAdapter :
    RecyclerView.Adapter<DetailHistoryPhotoAdapter.RsPhotoViewHolder>() {

    val data = mutableListOf<PhotoListModel?>()
    lateinit var adapterInterface: RsPhotoItemInterface

    fun setWithNewData(data: MutableList<PhotoListModel>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun setAdapterInterfacez(obj: RsPhotoItemInterface) {
        this.adapterInterface = obj
    }

    inner class RsPhotoViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemPhotoRsBinding = ItemPhotoRsBinding.bind(itemView)

        fun onBind(model: PhotoListModel?) {
            binding.tvMain.visibility = View.GONE
            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.ivMainImage.loadImageFromURL(binding.root.context, model?.url.toString())

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RsPhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_rs, parent, false)
        return RsPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RsPhotoViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RsPhotoItemInterface {
        fun onclick(model: PhotoListModel?)
    }
}