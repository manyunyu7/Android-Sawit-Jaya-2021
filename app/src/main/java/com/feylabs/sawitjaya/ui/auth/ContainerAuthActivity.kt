package com.feylabs.sawitjaya.ui.auth

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ActivityContainerAuthBinding
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.os.Build

import androidx.core.content.ContextCompat

import java.util.ArrayList
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.ui.ContainerUserHomeActivity

import com.feylabs.sawitjaya.ui.MainActivity
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import java.io.File


class ContainerAuthActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityContainerAuthBinding.inflate(layoutInflater)
    }

    lateinit var authViewModel: AuthViewModel

    var permissions = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val REQUEST_MULTIPLE_PERMISSIONS = 117

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        actionBar?.hide()
        val navController = findNavController(R.id.nav_host_auth)
        setupActionBarWithNavController(navController)
        val databasesDir = File(this.dataDir.toString() + "/databases")
        File(databasesDir, "MyDatabase.db").delete()
        checkPermissions()

        val factory = ServiceLocator.provideFactory(this)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        authViewModel.getProfileLocally()
        authViewModel.localProfileLD.observe(this, Observer {
            if (it.role == "3") {
                startActivity(Intent(this, ContainerUserHomeActivity::class.java))
            }
        })

        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {

    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(
                        this,
                        "Please Grant All Permission to Use All Feature",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}