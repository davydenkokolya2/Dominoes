/*
package com.example.calculator.remote.retrofit

import com.example.app.domain.AlbumModel
import com.example.app.domain.TracksWithAlbums
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create


class RemoteDataSourceRetrofit()  {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://stellio.ru/.inspiry/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    suspend fun getItunesAlbums(): Boolean {
        return (retrofit.create() as RetrofitService).getItunesAlbums()
    }

}*/
