package com.example.mockwebserverexample

import com.example.mockwebserverexample.domain.ProductRepository
import com.example.mockwebserverexample.infra.ProductRepositoryImpl
import com.example.mockwebserverexample.infra.ProductService
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ProductRepositoryImplIntegrationTest {

    private lateinit var repository: ProductRepository
    private lateinit var server: MockWebServer

    @Before
    fun setup(){
        server = MockWebServer()
        val gson = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("products/"))
            .addConverterFactory(gson).build()

        val service: ProductService = retrofit.create(ProductService::class.java)
        repository = ProductRepositoryImpl(service)
    }


    @After
    fun stopServer(){
        server.shutdown()
    }

    @Test
    fun `Given a status 200 when call getProducts() then return a list with products`(){

    }

    @Test
    fun `Given error 500 when call getProducts() then return a empty list`(){

    }

    private fun enqueueResponse(filename: String, headers: Map<String, String> = emptyMap()) {
        val path = filename
        val file = File(path).readText(Charsets.UTF_8)
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        server.enqueue(mockResponse.setBody(file))
    }
}