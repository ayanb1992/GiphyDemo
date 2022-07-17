package com.example.giphydemo.ui.main.common

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.giphydemo.R

open class BaseFragment : Fragment() {
    fun showErrorToast(message: String = "") {
        if (message.isNotEmpty()) {
            context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        } else {
            context?.let {
                Toast.makeText(
                    it,
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}