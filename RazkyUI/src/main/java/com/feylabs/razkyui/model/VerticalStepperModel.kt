package com.feylabs.razkyui.model

import com.feylabs.razkyui.enum.RazAlertType

data class VerticalStepperModel(
    val id: String,
    val number: String,
    val title: String,
    val description: String,
    val type: RazAlertType = RazAlertType.PRIMARY
)