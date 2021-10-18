package com.feylabs.sawitjaya.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.ActivityMainMenuContainerBinding
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import com.feylabs.sawitjaya.data.remote.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.Exception
import androidx.core.view.GravityCompat
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.ui.auth.ContainerAuthActivity


class MainMenuContainerActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainMenuContainerBinding


    val authViewModel: AuthViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuContainerBinding.inflate(layoutInflater)


        setContentView(binding.root)


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
                R.id.newsFragment, R.id.mNotificationFragment, R.id.scanQrCodeFragment,
                R.id.userHomeFragment, R.id.historyFragment, R.id.settingsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this);

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val navUserName: TextView = headerView.findViewById(R.id.tv_name)
        val navUserEmail: TextView = headerView.findViewById(R.id.tv_email)
        val navPicture: ImageView = headerView.findViewById(R.id.iv_profile_picture)

        val menu = navigationView.menu

        navigationView.setNavigationItemSelectedListener(this)


        val role = MyPreference(this).getPrefString("ROLE")
        if (role == "2") {
            menu.findItem(R.id.historyFragment).title = "Pekerjaan Saya"
            menu.findItem(R.id.nav_scanQR).setVisible(true)
        }

        if (role == "3") {
            menu.findItem(R.id.nav_scanQR).setVisible(false)
        }

        authViewModel.getProfileLocally()

        authViewModel.localProfileLD.observe(this, Observer {
            try {
                navUserName.text = it.name
                navUserEmail.text = it.email
                Timber.d("photo navdraw ${it.photo}")
                navPicture.loadImageFromURL(this, it.photo.toString())
            } catch (e: Exception) {

            }
        })


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_user_main_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        authViewModel.getProfileLocally()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.logout -> {
                MyPreference(this).clearPreferences()
                startActivity(Intent(this, ContainerAuthActivity::class.java))
            }
            R.id.newsFragment -> {
                findNavController(R.id.nav_host_fragment_content_user_main_menu).navigate(R.id.newsFragment)
            }
            R.id.settingsFragment -> {
                findNavController(R.id.nav_host_fragment_content_user_main_menu).navigate(R.id.settingsFragment)
            }
            R.id.historyFragment -> {
                findNavController(R.id.nav_host_fragment_content_user_main_menu).navigate(R.id.historyFragment)
            }
            R.id.userHomeFragment -> {
                findNavController(R.id.nav_host_fragment_content_user_main_menu).navigate(R.id.userHomeFragment)
            }
            R.id.mNotificationFragment -> {
                findNavController(R.id.nav_host_fragment_content_user_main_menu).navigate(R.id.mNotificationFragment)
            }
            R.id.nav_scanQR -> {
                findNavController(R.id.nav_host_fragment_content_user_main_menu).navigate(R.id.scanQrCodeFragment)
            }
        }
        return true
    }

}