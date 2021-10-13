package com.feylabs.sawitjaya.ui.user_history_tbs.scale

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.local.room.entity.NewsEntity
import com.feylabs.sawitjaya.databinding.ItemNewsBinding
import com.feylabs.sawitjaya.databinding.ItemNewsCompactBinding
import com.feylabs.sawitjaya.databinding.ItemRsScaleBinding
import com.feylabs.sawitjaya.utils.UIHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.UIHelper.renderHtmlToString

class RsScaleAdapter : RecyclerView.Adapter<RsScaleAdapter.NewsViewHolder>() {

    val data = mutableListOf<RsScaleModel>()
    lateinit var adapterInterface: RsScaleItemInterface

    fun setWithNewData(data: MutableList<RsScaleModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setAdapterInterfacez(obj: RsScaleItemInterface) {
        this.adapterInterface = obj
    }

    inner class NewsViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemRsScaleBinding = ItemRsScaleBinding.bind(itemView)

        fun onBind(model: RsScaleModel?) {
            binding.tvMain.text = model?.result.toString() + "Kg"
            binding.tvStaff.text = model?.inputedBy.toString()
            binding.tvDate.text = model?.inputedAt

            if (MyPreference(binding.root.context).getRole() == "3") {
                binding.btnDelete.visibility = View.GONE
            }

            adapterInterface.onSurface(model, binding)

            binding.btnDelete.setOnClickListener {
                adapterInterface.onclick(model, binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rs_scale, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RsScaleItemInterface {
        fun onclick(model: RsScaleModel?, binding: ItemRsScaleBinding)
        fun onSurface(model: RsScaleModel?, binding: ItemRsScaleBinding)
    }

}