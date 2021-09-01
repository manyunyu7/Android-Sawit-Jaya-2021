package com.feylabs.sawitjaya.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ActivityContainerUserHomeBinding

class ContainerUserHomeActivity : AppCompatActivity() {
    val binding by lazy { ActivityContainerUserHomeBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        actionBar?.hide()
        val navController = findNavController(R.id.nav_host_user_home)
        setupActionBarWithNavController(navController)
    }

}