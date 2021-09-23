package com.feylabs.sawitjaya.ui.rs.request.model

import android.net.Uri
import java.io.File

data class RsPhotoModel(
    val photoUri: Uri? = null,
    val photoFile: File? = null, //for glide
)
