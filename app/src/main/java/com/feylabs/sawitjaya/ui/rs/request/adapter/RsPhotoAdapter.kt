package com.feylabs.sawitjaya.ui.rs.request.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ItemPhotoRsBinding
import com.feylabs.sawitjaya.ui.rs.request.model.RsPhotoModel
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.ThumbnailsType
import com.feylabs.sawitjaya.utils.UIHelper.loadImage
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL

class RsPhotoAdapter(val type: ThumbnailsType = ThumbnailsType.LOADING_1) :
    RecyclerView.Adapter<RsPhotoAdapter.RsPhotoViewHolder>() {

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
            binding.tvMain.visibility = View.GONE
            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            if (model?.photoUri != null) {
                binding.ivMainImage.loadImageFromURL(
                    binding.root.context, model.photoUri.toString(),
                    ThumbnailsType.ADD_PHOTO_1
                )
            } else {
                binding.ivMainImage.loadImage(
                    binding.root.context, R.drawable.ic_add_photo_placeholder,
                    ThumbnailsType.ADD_PHOTO_1
                )
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