package com.example.kotlincorotines.coroutines_listener

import com.example.kotlincorotines.listener.APIInterface
import com.example.kotlincorotines.listener.RetrofitClientInstance

class Repository {
    /*Create handle for the RetrofitInstance interface*/
    val api = RetrofitClientInstance.retrofitInstance?.create(APIInterface::class.java)
    //simplified version of the retrofit call that comes from support with coroutines
    //Note that this does NOT handle errors, to be added
    suspend fun getAllHeros(url : String) = api?.getAllHeros(url)
    suspend fun getUpComingEvents(url : String) = api?.getUpComingEvents(url)

}