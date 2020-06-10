package com.sample.knowquake.di

import android.content.Context
import com.sample.knowquake.BuildConfig
import com.sample.knowquake.api.ApiService
import com.sample.knowquake.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.method(original.method, original.body)
            chain.proceed(requestBuilder.build())
        }
        httpClient.addInterceptor(NetworkConnectionInterceptor(context))
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.writeTimeout(60, TimeUnit.SECONDS)
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        httpClient.cache(cache)
        httpClient.retryOnConnectionFailure(true)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun providesNetworkService(retrofit: Retrofit): ApiService = retrofit
            .create(ApiService::class.java)
}