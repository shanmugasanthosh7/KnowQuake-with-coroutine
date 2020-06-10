package com.sample.knowquake.ui.earthquakedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.knowquake.provider.ResourceProvider
import com.sample.knowquake.rx.SchedulerProvider
import javax.inject.Inject

class EarthquakeDetailsViewModel
@Inject constructor(private val resourceProvider: ResourceProvider) : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title
    private val _timeDepth = MutableLiveData<String>()
    val timeDepth: LiveData<String>
        get() = _timeDepth
    private val _reviewStatus = MutableLiveData<String>()
    val reviewStatus: LiveData<String>
        get() = _reviewStatus
    private val _magnitude = MutableLiveData<String>()
    val magnitude: LiveData<String>
        get() = _magnitude
    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    fun setEarthquakeDetails(title: String, timeDepth: String, reviewStatus: String, magnitude: String, type: String) {
        _title.value = title
        _timeDepth.value = timeDepth
        _reviewStatus.value = reviewStatus
        _magnitude.value = magnitude
        _type.value = type
    }

}