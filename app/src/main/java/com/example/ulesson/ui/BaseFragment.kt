package com.example.ulesson.ui

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    protected fun onError(error: String?) {
//        parseError(error)
        error?.let {
            showToast(it)
        }
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}