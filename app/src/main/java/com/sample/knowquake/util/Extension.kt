package com.sample.knowquake.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> FragmentActivity.provideViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, viewModelFactory).get(T::class.java)
}