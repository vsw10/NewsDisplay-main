package com.husqvarna.newsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.husqvarna.newsapp.R
import com.husqvarna.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var toolBar: androidx.appcompat.widget.Toolbar

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toolBar = binding.toolbar
        initialise()
    }

    fun initialise() {

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val view: View = layoutInflater.inflate(R.layout.toolbar_content, toolBar)

        setUpClickListeners()
        hideUI()
    }

    fun setUpClickListeners() {
        toolBar.findViewById<AppCompatImageView>(R.id.bookmarked_toolbar).setOnClickListener {
            navController.navigate(R.id.action_HomeFragment_to_BookmarkedFragment)
        }

        toolBar.findViewById<AppCompatImageView>(R.id.search_toolbar).setOnClickListener {
            navController.navigate(R.id.action_HomeFragment_to_SearchNewsFragment)
        }
    }

    fun hideUI() {

        toolBar.findViewById<AppCompatEditText>(R.id.search_edit_text)?.let {
            it.isGone = true
        }
        toolBar.findViewById<AppCompatTextView>(R.id.bookmarked_title)?.let {
            it.isGone = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}