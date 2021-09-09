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

            binding.etStatus.text = data.statusDesc


            //Change Card Color
            val context = binding.root.context
            var color = R.color.colorGold
            when (data.status) {
                "0" -> color = R.color.bootstrapRed
                "1" -> color = R.color.colorGreen
                "3" -> color = R.color.colorWaiting
                "4" -> color = R.color.colorNeoBlue
                "2" -> color = R.color.colorPurple
            }
            binding.etStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.containerStatus.setCardBackgroundColor(ContextCompat.getColor(context,color))


            itemView.setOnClickListener {
                itemInterface.onclick(data)
            }

            val price = BusinessHelper.getEstimatedPrice(
                price = data.estPrice.toDouble(),
                margin = data.estMargin.toDouble(),
                weight = data.estWeight.toDouble()
            )


            binding.tvEstPrice.text = "Estimasi Harga : ${price}"

            Glide.with(binding.root)
                .load(data.userPhoto)
                .into(binding.ivProfilePicture)

            Glide.with(binding.root)
                .load(data.photo)
                .into(binding.ivMainImage)
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addData(model: MutableList<HistoryPagingModel.HistoryModel>) {
        data.addAll(model)
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


