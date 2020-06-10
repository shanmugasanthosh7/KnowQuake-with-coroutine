package com.sample.knowquake.di

import com.sample.knowquake.ui.earthquakedetails.EarthQuakeDetailsActivity
import com.sample.knowquake.ui.earthquakedetails.EarthquakeDetailsModule
import com.sample.knowquake.ui.earthquakes.EarthQuakeActivityModule
import com.sample.knowquake.ui.earthquakes.EarthQuakeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [EarthQuakeActivityModule::class])
    abstract fun bindEarthQuakeActivity(): EarthQuakeActivity

    @ContributesAndroidInjector(modules = [EarthquakeDetailsModule::class])
    abstract fun bindEarthQuakeDetailsActivity(): EarthQuakeDetailsActivity
}