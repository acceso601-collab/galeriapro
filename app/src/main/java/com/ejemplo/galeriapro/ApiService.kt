package com.ejemplo.galeriapro

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/list")
    suspend fun getImages(@Query("page") page: Int, @Query("limit") limit: Int = 30): List<ImageModel>

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
