package com.example.pokereloaded.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {

    companion object{
        private const val baseURL = "https://simplify-poke-api.herokuapp.com/"
//        private const val baseURL = "http://192.168.1.38:8080/"

        fun getRetrofitInstance(): Retrofit =
            Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

}