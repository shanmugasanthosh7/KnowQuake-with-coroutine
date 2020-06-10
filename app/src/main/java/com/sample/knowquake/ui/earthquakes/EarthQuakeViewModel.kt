package com.sample.knowquake.ui.earthquakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.knowquake.api.ApiService
import com.sample.knowquake.coroutine.CoroutineContextProvider
import com.sample.knowquake.provider.ResourceProvider
import com.sample.knowquake.result.Event
import com.sample.knowquake.vo.EarthQuake
import kotlinx.coroutines.*
import javax.inject.Inject

class EarthQuakeViewModel
@Inject constructor(
    private val apiService: ApiService,
    private val coroutineProvider: CoroutineContextProvider,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _isProgressShown = MutableLiveData<Event<Boolean>>()

    private val _earthQuake = MutableLiveData<EarthQuake>()

    private val _earthQuakeError = MutableLiveData<Throwable>()

    val earthQuake: LiveData<EarthQuake> get() = _earthQuake

    val earthQuakeError: LiveData<Throwable> get() = _earthQuakeError

    val isProgressShown: LiveData<Event<Boolean>> get() = _isProgressShown

    private val handler = CoroutineExceptionHandler { _, exception ->
        _earthQuakeError.value = exception
    }


    fun earthQuakeFeatures(limit: Int, offset: Int, isLoadMoreEnabled: Boolean) {
        // Maybe we can simplify the below steps with seal class with Success, Error and Loading
        // so we can use the single live data for all purpose.
        if (!isLoadMoreEnabled) _isProgressShown.value = Event(true)
        viewModelScope.launch(handler) {
            val features = withContext(coroutineProvider.io) {
                apiService.earthQuakeFeatures(
                    limit = limit,
                    offset = offset
                )
            }
            _earthQuake.value = features
            if (!isLoadMoreEnabled) _isProgressShown.value = Event(false)
        }
    }
}