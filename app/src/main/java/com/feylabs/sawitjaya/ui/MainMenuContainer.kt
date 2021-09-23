package com.feylabs.sawitjaya.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ActivityMainMenuContainerBinding
import com.feylabs.sawitjaya.databinding.NavHeaderUserMainMenuBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.utils.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.Exception

class MainMenuContainer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainMenuContainerBinding



    val authViewModel: AuthViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuContainerBinding.inflate(layoutInflater)


        setContentView(binding.root)

//        val factory = ServiceLocator.provideFactory(this)
//        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        getPriceData(true)


        authViewModel.pricesLiveData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })


        setSupportActionBar(binding.appBarUserMainMenu.toolbar)

        binding.appBarUserMainMenu.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView





        val navController = findNavController(R.id.nav_host_fragment_content_user_main_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userHomeFragment, R.id.historyFragment, R.id.settingsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navigationView : NavigationView  = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUserName: TextView = headerView.findViewById(R.id.tv_name)
        val navUserEmail: TextView = headerView.findViewById(R.id.tv_email)
        val navPicture: ImageView = headerView.findViewById(R.id.iv_profile_picture)




        authViewModel.getProfileLocally()
        authViewModel.localProfileLD.observe(this, Observer {
            try {
                navUserName.text = it.name
                navUserEmail.text = it.email
                Timber.d("photo navdraw ${it.photo}")
                Glide.with(this)
                    .load(it.photo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(navPicture)
            } catch (e: Exception) {

            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_main_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_user_main_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun getPriceData(saveLocally: Boolean) {
        authViewModel.getPrices(saveLocally)
    }

}