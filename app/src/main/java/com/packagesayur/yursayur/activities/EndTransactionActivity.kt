package com.packagesayur.yursayur.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.fragments.HomeFragment

class EndTransactionActivity : AppCompatActivity() {
    private lateinit var btnBackToHome2 : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_transaction)

        btnBackToHome2 = findViewById(R.id.btnBackToHome2)

        btnBackToHome2.setOnClickListener {
            finish()
        }
    }

}