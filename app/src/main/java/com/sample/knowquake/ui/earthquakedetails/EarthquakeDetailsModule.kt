package com.sample.knowquake.ui.earthquakedetails

import androidx.lifecycle.ViewModel
import com.sample.knowquake.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EarthquakeDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(EarthquakeDetailsViewModel::class)
    abstract fun bindEarthquakeDetailsViewModel(earthQuakeViewModel: EarthquakeDetailsViewModel): ViewModel

}