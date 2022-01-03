package com.example.medapp

import com.example.medapp.databinding.ActivityNavDrawerBinding



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*


class MainActivity : AppCompatActivity() {
    private lateinit var appbarc: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val nhf = supportFragmentManager.findFragmentById(R.id.fragment2) as NavHostFragment
        val topDest = setOf(R.id.homeFragment, R.id.page1Fragment, R.id.page2Fragment,R.id.endFragment)
        appbarc= AppBarConfiguration(topDest, binding.drawerLayout)
        setupActionBarWithNavController(nhf.navController, appbarc)
        //binding.NavigationView.setupWithNavController(nhf.navController)
        binding.NavigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.close()
            when (it.itemId) {
//                R.id.callAction -> {
//                    startActivity(Intent().setAction(Intent.ACTION_DIAL))
//                    true
//                }
//                R.id.endFragment{
//                    startActivity{}
//                }
                else -> {
                    it.onNavDestinationSelected(nhf.navController)
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment2).navigateUp(appbarc) || super.onSupportNavigateUp()
    }
    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }*/
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when(item.itemId){
            R.id.action_btn -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }*/
// 우측 상단 홈버튼 추가
}