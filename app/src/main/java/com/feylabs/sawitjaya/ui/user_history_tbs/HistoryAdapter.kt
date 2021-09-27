package com.feylabs.sawitjaya.ui.user_history_tbs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ItemGridRsBinding
import com.feylabs.sawitjaya.utils.BusinessHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.UIHelper.setColorStatus

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    val data = mutableListOf<HistoryPagingModel.HistoryModel>()
    lateinit var itemInterface: HistoryItemInterface


    fun setInterface(itemInterface: HistoryItemInterface) {
        this.itemInterface = itemInterface
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemGridRsBinding.bind(view)

        fun bind(data: HistoryPagingModel.HistoryModel) {
            binding.tvEstWeight.text = "${data.estWeight} Kg"
            binding.tvUserName.text = data.userName
            binding.status.build(
                type = data.status,
                text = data.statusDesc
            )

            //Change Card Color
            val context = binding.root.context
            val color = setColorStatus(data.status)

            itemView.setOnClickListener {
                itemInterface.onclick(data)
            }

            val price = BusinessHelper.getEstimatedPrice(
                price = data.estPrice.toDouble(),
                margin = data.estMargin.toDouble(),
                weight = data.estWeight.toDouble()
            )


            binding.tvEstPrice.text = "Estimasi Harga : ${price}"


            binding.ivProfilePicture.loadImageFromURL(binding.root.context, data.userPhoto)
            binding.ivMainImage.loadImageFromURL(binding.root.context, data.photo)
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addData(model: MutableList<HistoryPagingModel.HistoryModel>) {
        data.addAll(model)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_rs, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface HistoryItemInterface {
        fun onclick(model: HistoryPagingModel.HistoryModel)
    }

}


