package com.feylabs.sawitjaya.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.LayoutCustomButtonWithLeftImageBinding
import com.feylabs.sawitjaya.utils.UIHelper.loadImage

class CustomMenuContainer : FrameLayout {
    private var text: String = ""
    private var containerBackgroundColor: Int = R.color.bs_alert_primary
    private var imageDrawable: Int = R.color.bs_alert_primary

    private var binding: LayoutCustomButtonWithLeftImageBinding =
        LayoutCustomButtonWithLeftImageBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context) : super(context) {
        initView(context)
    }

    init { // inflate binding and add as view
        addView(binding.root)
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
        text(text)
        image(imageDrawable)
        background(containerBackgroundColor)
    }

    fun text(text: String) {
        this.text = text
        binding.tvText.text = text
    }

    fun image(image: Int) {
        this.imageDrawable = image
        binding.ivImg.loadImage(context, image)
    }

    fun background(backgroundColor: Int) {
        this.containerBackgroundColor = backgroundColor
        binding.rootContainer.setBackgroundColor(
            ContextCompat.getColor(
                context,
                containerBackgroundColor
            )
        )
    }

    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RazCustomInfoSingleLine)
        typedArray.apply {
            text = getString(R.styleable.CustomButtonLeftImage_customButtonLeftText) ?: text
        }

        typedArray.recycle()
    }
}