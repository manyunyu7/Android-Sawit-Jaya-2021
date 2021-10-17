package com.feylabs.razkyui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.razkyui.databinding.RazCustomInfoVertBinding

class RazCustomInfoVert : FrameLayout {
    var title: String = ""
    var value: String = ""
    val binding = RazCustomInfoVertBinding.inflate(LayoutInflater.from(context), this, true)


    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        extractAttributes(attributeSet)
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        extractAttributes(attributeSet)
        initView(context)
    }

    private fun initView(context: Context?) {
        title(title)
        value(value)
    }

    fun build(title: String, value: String) {
        title(title)
        value(value)
    }

    fun title(title: String) {
        this.title = title
        binding.tvInfoTitle.text = title
    }

    fun value(value: String) {
        this.value = value
        binding.tvInfoValue.text = value
    }

    fun fontSizeSp(size: Float) {
        binding.tvInfoTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        binding.tvInfoValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun fontSizeSp(titleSize: Float, valueSize: Float) {
        binding.tvInfoTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize)
        binding.tvInfoValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, valueSize)
    }

    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RazCustomInfo)
        title = typedArray.getString(R.styleable.RazCustomInfo_customInfoTitle) ?: title
        value = typedArray.getString(R.styleable.RazCustomInfo_customInfoContent) ?: value
        typedArray.recycle()
    }
}