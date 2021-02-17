package com.werainkhatri.contextualcards.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface API {
    @GET("fefcfbeb-5c12-4722-94ad-b8f92caad1ad")
    suspend fun getData() : Response<APIResponse>

    companion object {
        operator fun invoke() : API {
            return Retrofit.Builder()
                .baseUrl("https://run.mocky.io/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }
    }
}