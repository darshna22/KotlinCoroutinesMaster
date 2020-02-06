package com.example.kotlincorotines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincorotines.coroutines_listener.Repository
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callRetrofit()
    }

    private fun callRetrofit() {
        GlobalScope.launch(Dispatchers.Main) {
            val time = measureTimeMillis {
                val retrievedDataOne =
                    async { Repository().getAllHeros("https://simplifiedcoding.net/demos/marvel/") }
                val retrievedDataTwo =
                    async { Repository().getUpComingEvents("https://api.coingecko.com/api/v3/events") }
                println("The p answer is " + retrievedDataOne.await() + "\n" + retrievedDataTwo.await())
            }
            println("Completed p in $time ms")
        }
    }


}
