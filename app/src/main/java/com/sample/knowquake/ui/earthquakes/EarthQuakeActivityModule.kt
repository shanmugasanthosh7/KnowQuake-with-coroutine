package com.sample.knowquake.ui.earthquakes

import androidx.lifecycle.ViewModel
import com.sample.knowquake.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EarthQuakeActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(EarthQuakeViewModel::class)
    abstract fun bindEarthQuakeViewModel(earthQuakeViewModel: EarthQuakeViewModel): ViewModel

}