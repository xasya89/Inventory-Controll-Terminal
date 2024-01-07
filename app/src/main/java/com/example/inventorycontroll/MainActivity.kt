package com.example.inventorycontroll

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.common.viewModels.KeyListenerViewModel
import com.example.inventorycontroll.common.viewModels.ShopViewModel
import com.example.inventorycontroll.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    @Inject lateinit var shopService: ShopService
    private val shopViewModel by viewModels<ShopViewModel>()
    private val keyListenerVm by viewModels<KeyListenerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_inventory_loading, R.id.nav_synchronization
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onStart() {
        super.onStart()

        shopViewModel.getShops()
        shopViewModel.shops.observe(this, {
            val spinner = binding.navView.getHeaderView(0) .findViewById<Spinner>(R.id.selectShopSpinner)
            val items = it.map { it.name }
            val adapter = ArrayAdapter<String>(this@MainActivity, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, items)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    shopViewModel.setSelectShop(shopViewModel.shops.value?.get(p2)!!)
                    val navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                    val dest = navController.currentDestination
                    when(dest!!.id){
                        R.id.nav_inventory_loading -> navController.navigate(R.id.nav_home)
                        R.id.nav_host_fragment_content_main -> navController.navigate(R.id.nav_inventory_loading)
                        R.id.findGoodFragment -> navController.navigate(R.id.nav_inventory_loading)
                        R.id.nav_synchronization -> navController.navigate(R.id.nav_home)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        } )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if(event?.keyCode==115){
            val view = this.currentFocus
            view?.clearFocus()
            if(view==null) return false
            val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return false
        }
        keyListenerVm.appendKey(event)
        //if(event?.keyCode==KeyEvent.KEYCODE_ENTER) return false;
        return super.dispatchKeyEvent(event)
    }
}