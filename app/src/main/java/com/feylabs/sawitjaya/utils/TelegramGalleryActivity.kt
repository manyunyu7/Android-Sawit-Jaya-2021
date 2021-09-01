package com.feylabs.sawitjaya.utils

import com.tangxiaolv.telegramgallery.GalleryActivity

import android.content.Intent
import androidx.fragment.app.Fragment

import com.tangxiaolv.telegramgallery.GalleryConfig


object TelegramGalleryActivity : GalleryActivity() {
    /**
     * Open gallery with fragment starting the the activity to use onActivityResult
     * in fragment
     */
    fun openActivity(fragment: Fragment, requestCode: Int, config: GalleryConfig?) {
        val intent = Intent(fragment.context, GalleryActivity::class.java)
        intent.putExtra("GALLERY_CONFIG", config)
        fragment.startActivityForResult(intent, requestCode)
    }
}