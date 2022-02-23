package com.example.mockwebserverexample.infra

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL: String = "https://api.products.org/3/" // Fake API
    private val client = OkHttpClient.Builder()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(client.build())
        .build()

    fun<T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

}