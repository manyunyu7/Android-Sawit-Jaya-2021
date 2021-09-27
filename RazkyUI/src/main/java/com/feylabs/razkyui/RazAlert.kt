package com.feylabs.razkyui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.feylabs.razkyui.databinding.RazAlertBinding
import com.feylabs.razkyui.enum.RazAlertType

class RazAlert : FrameLayout {

    private var title = ""
    private var value = ""
    val binding = RazAlertBinding.inflate(LayoutInflater.from(context), this, true)


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
        val view = inflater.inflate(R.layout.raz_alert, this)
        binding.root.setBackgroundResource(R.drawable.raz_alert_container_primary)
        setTitle(title)
        setContent(value)
    }

    fun changeColor(status: RazAlertType) {
        var backgroundRes = R.drawable.raz_alert_container_primary
        var textColor = R.color.bs_alert_primary
        when (status) {
            RazAlertType.SUCCESS -> {
                textColor = R.color.text_success
                backgroundRes = R.drawable.raz_alert_container_success
            }
            RazAlertType.DANGER -> {
                textColor = R.color.text_danger
                backgroundRes = R.drawable.raz_alert_container_danger
            }
            RazAlertType.WARNING -> {
                textColor = R.color.text_warning
                backgroundRes = R.drawable.raz_alert_container_warning
            }
            RazAlertType.PRIMARY -> {
                textColor = R.color.text_primary
                backgroundRes = R.drawable.raz_alert_container_primary
            }
        }
        binding.tvAlertContent.setTextColor(textColor)
        binding.tvAlertTitle.setTextColor(textColor)
        binding.root.setBackgroundResource(backgroundRes)

    }

    fun build(title: String, content: String, type: RazAlertType = RazAlertType.PRIMARY) {
        setTitle(title)
        setContent(content)
        changeColor(type)
    }

    fun setTitle(title: String) {
        this.title = title
        binding.tvAlertTitle.text = title
    }

    fun setContent(content: String) {
        this.value = content
        binding.tvAlertContent.text = content
    }

    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RazAlertContainer)
        title = typedArray.getString(R.styleable.RazAlertContainer_alertTitle) ?: title
        value = typedArray.getString(R.styleable.RazAlertContainer_alertContent) ?: value
        typedArray.recycle()
    }


}

