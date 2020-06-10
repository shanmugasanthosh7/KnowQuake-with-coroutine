package com.sample.knowquake.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.knowquake.result.Event

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: MutableLiveData<Boolean>) {
    if (visible.value!!) view.visibility = View.VISIBLE else view.visibility = View.GONE
}

@BindingAdapter("visibleUnless")
fun visibleUnless(view: View, visible: MutableLiveData<Boolean>) {
    if (visible.value!!) view.visibility = View.GONE else view.visibility = View.VISIBLE
}