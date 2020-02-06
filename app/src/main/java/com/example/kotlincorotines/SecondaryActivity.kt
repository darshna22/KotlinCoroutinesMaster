package com.example.kotlincorotines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import com.example.kotlincorotines.listener.APIInterface
import com.example.kotlincorotines.listener.RetrofitClientInstance
import com.example.kotlincorotines.utility.AndroidAppUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class SecondaryActivity : AppCompatActivity() {
    private var apiBoolean1 = false
    private var apiBoolean2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callRetrofit()
    }

    private fun callRetrofit() {
        val time = measureTimeMillis {
            AndroidAppUtils.showProgressDialog(this, "Please wait...", false)
            /*Create handle for the RetrofitInstance interface*/
            val api = RetrofitClientInstance.retrofitInstance?.create(APIInterface::class.java)

            //now making the call object
            //Here we are using the api method that we created inside the api interface
            val call = api?.getAllHeros("https://simplifiedcoding.net/demos/marvel/")
            val call_ = api?.getUpComingEvents("https://api.coingecko.com/api/v3/events")
            call?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = try {
                    val myRes: String = response.body()!!.string()
                    apiBoolean1 = true
                    if (apiBoolean1 && apiBoolean2)
                        AndroidAppUtils.hideProgressDialog()
                    else {
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SecondaryActivity, t.message, Toast.LENGTH_SHORT).show()
                    apiBoolean1 = true
                    if (apiBoolean1 && apiBoolean2)
                        AndroidAppUtils.hideProgressDialog()
                    else {
                    }

                }

            })

            call_?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = try {
                    val myRes: String = response.body()!!.string()
                    apiBoolean2 = true
                    closeProgressDialog()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SecondaryActivity, t.message, Toast.LENGTH_SHORT).show()
                    apiBoolean2 = true
                    closeProgressDialog()
                }

            })
        }
        println("Completed in $time ms")
    }

    fun closeProgressDialog() {
        if (apiBoolean1 && apiBoolean2)
            AndroidAppUtils.hideProgressDialog()
        else {
        }
    }

}
