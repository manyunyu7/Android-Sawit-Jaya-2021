package com.feylabs.sawitjaya.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ActivityContainerUserHomeBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel

class ContainerUserHomeActivity : AppCompatActivity() {
    val binding by lazy { ActivityContainerUserHomeBinding.inflate(layoutInflater)}

    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val factory = ServiceLocator.provideFactory(this)
        authViewModel = ViewModelProvider(this,factory).get(AuthViewModel::class.java)

        supportActionBar?.hide()
        actionBar?.hide()
        val navController = findNavController(R.id.nav_host_user_home)
        setupActionBarWithNavController(navController)


        authViewModel.pricesLiveData.observe(this, Observer {
            when(it){
                is Resource.Success->{
                }
                is Resource.Error->{
                }
                is Resource.Loading->{
                }
            }
        })

    }





}