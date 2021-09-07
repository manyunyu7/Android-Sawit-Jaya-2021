package com.feylabs.sawitjaya.data.remote.request


import com.google.gson.annotations.SerializedName

data class GetRDSPaginationReq(
    @SerializedName("is_paginate")
    val isPaginate: String?,
    @SerializedName("page")
    val page: String?,
    @SerializedName("per_page")
    val perPage: String?
)