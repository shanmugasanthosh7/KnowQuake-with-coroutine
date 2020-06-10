package com.sample.knowquake.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.sample.knowquake.QuakeApplication
import com.sample.knowquake.coroutine.CoroutineContextProvider
import com.sample.knowquake.provider.ResourceProvider
import com.sample.knowquake.provider.ResourceProviderImpl
import com.sample.knowquake.worker.WorkerBindingModule
import com.sample.knowquake.rx.AppSchedulerProvider
import com.sample.knowquake.rx.SchedulerProvider
import com.sample.knowquake.util.AppExecutors
import com.sample.knowquake.vo.Constants
import com.sample.knowquake.vo.DataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, ViewModelModule::class, WorkerBindingModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideApplication(application: QuakeApplication): QuakeApplication = application

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun providePrefs(app: Application): SharedPreferences =
        app.applicationContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePrefsEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()

    @Provides
    fun provideWorkerManger(app: Application): WorkManager {
        return WorkManager.getInstance(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Singleton
    @Provides
    fun provideCoroutineProvider(): CoroutineContextProvider = CoroutineContextProvider()

    @Singleton
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider = ResourceProviderImpl(context)

    @Singleton
    @Provides
    fun provideDataStore(): DataStore = DataStore()

    @Provides
    fun provideAppExecutors(): AppExecutors = AppExecutors()

}