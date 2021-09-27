package com.feylabs.razkyui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.razkyui.RazVerticalStepperAdapter.*
import com.feylabs.razkyui.databinding.RazAlertBinding
import com.feylabs.razkyui.databinding.RazVerticalStepperBinding
import com.feylabs.razkyui.enum.RazAlertType
import com.feylabs.razkyui.model.VerticalStepperModel

class RazVerticalStepper : FrameLayout {

    private var title = ""
    private var value = ""
    val binding = RazVerticalStepperBinding.inflate(LayoutInflater.from(context), this, true)
    val adapter by lazy { RazVerticalStepperAdapter() }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context)
        extractAttributes(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initView(context)
        extractAttributes(attributeSet)
    }

    private fun initView(context: Context?) {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.raz_vertical_stepper, this)
        binding.rvStepper.adapter = adapter
        binding.rvStepper.layoutManager = LinearLayoutManager(context)
    }


    fun addData(data: VerticalStepperModel) {
        adapter.addData(data)
    }

    fun setStepperInterface(adapterInterface: VerticalStepperInterface) {
        adapter.setInterface(adapterInterface)
    }


    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RazAlertContainer)
        title = typedArray.getString(R.styleable.RazAlertContainer_alertTitle) ?: title
        value = typedArray.getString(R.styleable.RazAlertContainer_alertContent) ?: value
        typedArray.recycle()
    }


}

