package com.feylabs.razkyui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.razkyui.databinding.ItemVerticalStepperBinding
import com.feylabs.razkyui.model.VerticalStepperModel

class RazVerticalStepperAdapter :
    RecyclerView.Adapter<RazVerticalStepperAdapter.RazVerticalStepperVH>() {

    lateinit var adapterInterface: VerticalStepperInterface
    private val adapterData = mutableListOf<VerticalStepperModel>()

    inner class RazVerticalStepperVH(v: View) : RecyclerView.ViewHolder(v) {
        val binding = ItemVerticalStepperBinding.bind(v)
        fun bind(data: VerticalStepperModel) {
            binding.item.build(
                title = data.title,
                content = data.description
            )

            binding.root.setOnClickListener {
                if (adapterInterface != null) {
                    adapterInterface.onclick(data)
                }
            }


        }
    }

    fun clearData(){
        this.adapterData.clear()
        notifyDataSetChanged()
    }

    fun addData(data: VerticalStepperModel) {
        this.adapterData.add(data)
        notifyItemInserted(adapterData.size)
    }

    fun setInterface(adapterInterface: VerticalStepperInterface) {
        this.adapterInterface = adapterInterface
    }

    fun setData(newData: MutableList<VerticalStepperModel>) {
        this.adapterData.clear()
        this.adapterData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RazVerticalStepperVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vertical_stepper, parent, false)
        return RazVerticalStepperVH(view)
    }

    override fun onBindViewHolder(holder: RazVerticalStepperVH, position: Int) {
        holder.bind(adapterData[position])
    }

    override fun getItemCount(): Int {
        return adapterData.size
    }

    interface VerticalStepperInterface {
        fun onclick(model: VerticalStepperModel)
    }
}

