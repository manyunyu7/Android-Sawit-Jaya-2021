package com.feylabs.sawitjaya.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.remote.response.RsChatResponseItem
import com.feylabs.sawitjaya.databinding.ItemGridRsBinding
import com.feylabs.sawitjaya.databinding.ItemRschatBinding
import com.feylabs.sawitjaya.utils.BusinessHelper
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.utils.UIHelper.setColorStatus

class RsChatAdapter : RecyclerView.Adapter<RsChatAdapter.RsChatViewHolder>() {

    val data = mutableListOf<RsChatResponseItem>()
    lateinit var itemInterface: RsChatItemInterface


    fun setInterface(itemInterface: RsChatItemInterface) {
        this.itemInterface = itemInterface
    }

    inner class RsChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemRschatBinding.bind(view)

        fun bind(data: RsChatResponseItem) {
            val mContext = binding.root.context
            val userID = MyPreference(binding.root.context).getUserID()

            // if chat if from current user
            if (userID == data.idSender.toString()) {
                binding.containerChatRight.visibility = View.VISIBLE
                binding.containerChatLeft.visibility = View.GONE

                binding.ivProfileRight.loadImageFromURL(mContext, data.senderPhoto)
                binding.tvUserNameRight.text = "Anda"
                binding.tvRight.text = data.message
                binding.tvTimeRight.text = "tes" + data.createdAt.toString()
            }

            // if chat is not from current user
            if (userID != data.idSender.toString()) {
                binding.containerChatLeft.visibility = View.VISIBLE
                binding.containerChatRight.visibility = View.GONE

                binding.ivProfileLeft.loadImageFromURL(mContext, data.senderPhoto)
                binding.tvUserNameLeft.text = data.senderName
                binding.tvLeft.text = data.message
                binding.tvTimeLeft.text = data.createdAt.toString()
            }

        }
    }


    fun addData(model: MutableList<RsChatResponseItem>) {
        this.data.clear()
        data.addAll(model)
        notifyDataSetChanged()
    }

    fun insertItem(model: RsChatResponseItem) {
        data.add(model)
        notifyItemInserted(data.size + 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RsChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rschat, parent, false)
        return RsChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RsChatViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RsChatItemInterface {
        fun onclick(model: RsChatResponseItem)
    }

}


