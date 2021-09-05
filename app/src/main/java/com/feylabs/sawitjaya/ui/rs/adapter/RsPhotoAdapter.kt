package com.feylabs.sawitjaya.ui.rs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ItemPhotoRsBinding
import com.feylabs.sawitjaya.ui.rs.model.RsPhotoModel

class RsPhotoAdapter : RecyclerView.Adapter<RsPhotoAdapter.RsPhotoViewHolder>() {

    val data = mutableListOf<RsPhotoModel?>()
    lateinit var adapterInterface: RsPhotoItemInterface

    fun setWithNewData(data: MutableList<RsPhotoModel>) {
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

        fun onBInd(model: RsPhotoModel?) {
//            binding.tvMain.text = model?.title
            binding.tvMain.visibility = View.GONE
            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }


            if (model?.photoUri != null) {
                Glide.with(binding.root)
                    .load(model?.photoUri)
                    .placeholder(R.drawable.ic_add_photo_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivMainImage)
            } else {
                Glide.with(binding.root)
                    .load(R.drawable.ic_placeholder)
                    .placeholder(R.drawable.ic_add_photo_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivMainImage)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RsPhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_rs, parent, false)
        return RsPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RsPhotoViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RsPhotoItemInterface {
        fun onclick(model: RsPhotoModel?)
    }
}