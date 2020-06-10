package com.sample.knowquake.api

import com.sample.knowquake.vo.EarthQuake
import com.sample.knowquake.vo.FeatureProperties
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("fdsnws/event/1/query")
    suspend fun earthQuakeFeatures(
        @Query("format") format: String = "geojson", // 'geojson' return JSON Format
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String = "time", // 'time' is DESC, 'time-asc' - ASC
        @Query("offset") offset: Int
    ): EarthQuake

    @GET("earthquakes/feed/v1.0/summary/all_hour.geojson")
    suspend fun lastAnHourEarthQuake(): EarthQuake

    @GET("fdsnws/event/1/query")
    fun moreDetail(
        @Query("eventid") eventId: String,
        @Query("format") format: String = "geojson"  // 'geojson' return JSON Format
    ): Observable<FeatureProperties>

}