package com.feylabs.sawitjaya.ui.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sawitjaya.databinding.FragmentScanQrCodeBinding
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.CodeScanner

import android.widget.Toast

import android.media.MediaPlayer
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.ui.user_history_tbs.HistoryFragmentDirections
import com.feylabs.sawitjaya.utils.DialogUtils
import timber.log.Timber
import java.lang.Exception


class ScanQrCodeFragment : BaseFragment() {

    var _binding: FragmentScanQrCodeBinding? = null
    val binding get() = _binding as FragmentScanQrCodeBinding

    lateinit var mCodeScanner: CodeScanner

    override fun initUI() {

        mCodeScanner = CodeScanner(requireContext(), binding.scannerView)
        mCodeScanner.startPreview()
        with(mCodeScanner) {

            setDecodeCallback { result ->
                requireActivity().runOnUiThread(
                    Runnable { scanned(result.toString()) })
            }
        }

        binding.scannerView.setOnClickListener { mCodeScanner.startPreview() }

    }

    private fun scanned(result: String) {
        try {
            Timber.d("result scanned $result")
            val res1 = result.split("-").toTypedArray()
            val idRS = res1[2]

            DialogUtils.showCustomDialog(
                context = requireContext(),
                title = "QR Code Dikenali",
                message = "Apakah Anda Ingin Menuju Halaman Detail Transaksi Ini ? ",
                positiveAction = Pair(getString(R.string.dialog_ok), {
                    goToFragmentDetail(idRS)
                }),
                negativeAction = Pair(getString(R.string.dialog_cancel), {
                    mCodeScanner.startPreview()
                }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        } catch (x: Exception) {
            DialogUtils.showCustomDialog(
                context = requireContext(),
                title = "QR Code Tidak Dikenali",
                message = "Silakan Scan QR Ulang, pastikan QR Code yang discan adalah transaksi",
                positiveAction = Pair(getString(R.string.dialog_ok), {
                    mCodeScanner.startPreview()
                }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }

    private fun goToFragmentDetail(id: String) {
        val directions =
            ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToDetailHistoryFragment(
                id.toString()
            )
        findNavController()?.navigate(directions)
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
        _binding = FragmentScanQrCodeBinding.inflate(inflater)
        return binding.root
    }


}