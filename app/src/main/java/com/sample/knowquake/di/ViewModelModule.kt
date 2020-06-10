package com.sample.knowquake.di

import androidx.lifecycle.ViewModelProvider
import com.sample.knowquake.viewmodelfactory.EqViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: EqViewModelFactory): ViewModelProvider.Factory
}