package com.example.githubapps.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.example.githubapps.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        cv_language.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            cv_language.id -> {
                val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(i)
            }
        }
    }
}