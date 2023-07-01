package com.packagesayur.yursayur.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ActivitySplashBinding
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private var mShouldFinish = false
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!NetworkUtils.isInternetAvailable(this)) {
            showNoInternetPopup()
        } else {
            startMainScreen()
        }


    }

    private fun showNoInternetPopup() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.item_internet_connection, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        val btnDismiss = dialogView.findViewById<Button>(R.id.btnTutup)
        btnDismiss.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }

        alertDialog.show()
    }

    private fun startMainScreen() {
        setupViewModel()
        Handler(Looper.getMainLooper()).postDelayed({
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    binding.imageView,
                    "logoLogin"
                )
            authViewModel.getUserKey().observe(this){
                if (it.isNullOrEmpty()) {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java), optionsCompat.toBundle())
                } else {
                    startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java), optionsCompat.toBundle())
                }
            }
            mShouldFinish = true
        }, DELAY)
    }

    override fun onStop() {
        super.onStop()
        if (mShouldFinish) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
    }
    companion object {
        const val DELAY = 5000L
    }
}