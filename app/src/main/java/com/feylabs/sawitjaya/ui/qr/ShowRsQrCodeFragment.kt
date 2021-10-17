package com.feylabs.sawitjaya.ui.qr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentShowRsQrCodeBinding
import com.feylabs.sawitjaya.ui.base.BaseFragment
import android.graphics.Bitmap

import androidmads.library.qrgenearator.QRGContents

import androidmads.library.qrgenearator.QRGEncoder

import android.R.id
import androidx.navigation.fragment.navArgs


class ShowRsQrCodeFragment : BaseFragment() {

    var _binding: FragmentShowRsQrCodeBinding? = null
    val binding get() = _binding as FragmentShowRsQrCodeBinding

    val args: ShowRsQrCodeFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowRsQrCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun initUI() {
        hideActionBar()
        binding.toolbarTitle.text = "QR Code ${args.transactionCode}"
        val str = args.transactionCode
        val res = str.split("[-]").dropLastWhile { it.isEmpty() }.toTypedArray()
        val data = res[0]
        val qre = QRGEncoder(str, null, QRGContents.Type.TEXT, 500)
        val qrbm = qre.bitmap
        binding.showQr.setImageBitmap(qrbm)
        binding.tvOwnerName.text = args.ownerName
        binding.tvTransactionCode.text = args.transactionCode
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}