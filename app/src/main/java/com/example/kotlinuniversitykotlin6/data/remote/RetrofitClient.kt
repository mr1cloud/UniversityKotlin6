package com.example.kotlinuniversitykotlin6.data.remote

import com.example.kotlinuniversitykotlin6.data.remote.api.PicsumApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build()

    val picsumApi: PicsumApi =
        Retrofit.Builder().baseUrl("https://picsum.photos/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PicsumApi::class.java)
}