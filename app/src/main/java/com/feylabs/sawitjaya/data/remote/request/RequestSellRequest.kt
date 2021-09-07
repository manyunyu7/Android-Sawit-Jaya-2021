package com.feylabs.sawitjaya.data.remote.request


import com.google.gson.annotations.SerializedName
import java.io.File

data class RequestSellRequest(
    @SerializedName("additional_contact")
    val additionalContact: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("contact")
    val contact: String?,
    @SerializedName("est_weight")
    val estWeight: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("long")
    val long: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("upload_file")
    val uploadFile: MutableList<File?>?
)