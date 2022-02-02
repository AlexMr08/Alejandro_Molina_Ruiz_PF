package com.example.practica_final.admin

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_final.MainActivity
import com.example.practica_final.R
import com.example.practica_final.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding
    val navController by lazy { findNavController(R.id.nav_host_fragment_content_admin) }

    val categorias by lazy { resources.getStringArray(R.array.categorias).toList()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdmin.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.adminHomeFragment, R.id.adminEventFragment, R.id.adminOrderFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        navView.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.admin_navigation_home -> {
//                    navController.navigate(R.id.adminHomeFragment)
//                    true
//                }
//                R.id.admin_navigation_event -> {
//                    navController.navigate(R.id.adminEventFragment)
//                    true
//                }
//                R.id.admin_navigation_algo -> {
//                    Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun FAB_manager(mode: Int, listener: (View) -> Unit) {
        //si no necesitas añadir cosas a la lista desde un fragmento quita el listener
        when (mode) {
            1 -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_baseline_post_add_24)
                    setOnClickListener {
                        navController.navigate(R.id.action_adminHomeFragment_to_adminAddCardFragment)
                    }
                }
            }
            2 -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_baseline_add_location_24)
                }
            }
            3->{
                (binding.appBarAdmin.fab).apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_baseline_add_24)
                    setOnClickListener(listener)
                }
            }
            else -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.INVISIBLE
                }
            }
        }
    }
}