package com.feylabs.sawitjaya.ui.user_history_tbs.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.databinding.FragmentUpdateRsBinding
import com.feylabs.sawitjaya.injection.ServiceLocator.REAL_URL
import com.feylabs.sawitjaya.utils.base.BaseFragment


import android.webkit.WebView

import android.graphics.Bitmap





class UpdateRsFragment : BaseFragment() {

    var _binding: FragmentUpdateRsBinding? = null
    val binding get() = _binding as FragmentUpdateRsBinding

    private val args: UpdateRsFragmentArgs by navArgs()


    override fun initUI() {
        viewVisible(binding.includeLoading.root)
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateRsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mWebView = binding.webview
        val rsId = args.rsID
        val userToken = MyPreference(requireContext()).getToken()
        val userID = MyPreference(requireContext()).getUserID()

        mWebView.loadUrl("${REAL_URL}mobile_raz/request-sell/$rsId/edit/?user_id=${userID}")
        // Enable Javascript
        val webSettings: WebSettings = mWebView.getSettings()
        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.webViewClient = WebViewClient()
        mWebView.settings.setDomStorageEnabled(true)
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        mWebView.clearCache(true)


        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                viewGone(binding.includeLoading.root)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                viewVisible(binding.includeLoading.root)
            }

            override fun onReceivedError(
                view: WebView,
                errorCod: Int,
                description: String,
                failingUrl: String
            ) {
                viewGone(binding.includeLoading.root)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }
        }
    }


}