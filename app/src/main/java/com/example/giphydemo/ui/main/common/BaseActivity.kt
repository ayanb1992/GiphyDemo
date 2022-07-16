package com.example.giphydemo.ui.main.common

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.giphydemo.R

open class BaseActivity: AppCompatActivity() {

    open fun showErrorToast() {
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
    }

}