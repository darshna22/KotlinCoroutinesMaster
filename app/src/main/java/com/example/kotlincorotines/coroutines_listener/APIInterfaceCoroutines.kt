package com.example.kotlincorotines.coroutines_listener

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Purpose of this class to declare and access all API response.
 * Created by Darshna Kumari on 28/05/2019.
 */
interface APIInterfaceCoroutines {
    @GET("{fullUrl}")
    suspend fun getAllHeros(@Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ResponseBody>

    @GET("{fullUrl}")
    suspend fun getUpComingEvents(@Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ResponseBody>

}