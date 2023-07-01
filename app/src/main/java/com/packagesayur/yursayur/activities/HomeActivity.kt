package com.packagesayur.yursayur.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.fragments.ChatFragment
import com.packagesayur.yursayur.fragments.HomeFragment
import com.packagesayur.yursayur.fragments.NotificationFragment
import com.packagesayur.yursayur.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadFragment(HomeFragment())

        bottomNavigationView = findViewById(R.id.bottomNavigation) as BottomNavigationView
        floatingActionButton = findViewById(R.id.floatingActionButton) as FloatingActionButton


        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_pesan -> {
                    loadFragment(ChatFragment())
                    true
                }
                R.id.nav_notifikasi -> {
                    loadFragment(NotificationFragment())
                    true
                }
                R.id.nav_profil -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {
                    loadFragment(HomeFragment())
                    true
                }
            }
        }

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}