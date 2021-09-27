package com.feylabs.razkyui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.feylabs.razkyui.R
import com.feylabs.razkyui.databinding.RazCustomInfoBinding
import com.feylabs.razkyui.databinding.SawitCustomStatusBinding

class SawitCustomStatus : FrameLayout {

    private var title: String = ""
    private var color: Int = R.color.bs_alert_primary

    val binding = SawitCustomStatusBinding.inflate(LayoutInflater.from(context), this, true)

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
        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.sawit_custom_status, this)

        title(title)

    }

    fun build(
        type: String,
        text: String
    ) {
        var colorText = R.color.bs_alert_primary_stroke
        var colorCard = R.color.bs_alert_primary

        when (type) {
            "0" -> {
                colorText = R.color.white
                colorCard = R.color.bg_danger
            }
            "1" -> {
                colorText = R.color.white
                colorCard = R.color.bg_success
            }
            "3" -> {
                colorText = R.color.white
                colorCard = R.color.bg_warning
            }
            "4" -> {
                colorText = R.color.white
                colorCard = R.color.bg_primary
            }
            "2" -> {
                colorText = R.color.white
                colorCard = R.color.bg_yellow
            }
        }
        binding.tvStatus.setTextColor(colorText)
        binding.containerStatus.setCardBackgroundColor(ContextCompat.getColor(context, colorCard))
    }

    fun title(title: String) {
        this.title = title
        binding.tvStatus.text = title
    }


    fun fontSizeSp(size: Float) {
        binding.tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun fontSizeSp(titleSize: Float, valueSize: Float) {
        binding.tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize)
    }

    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SawitCustomStatus)
        title = typedArray.getString(R.styleable.SawitCustomStatus_sawitCustomTitle) ?: title
        typedArray.recycle()
    }

}