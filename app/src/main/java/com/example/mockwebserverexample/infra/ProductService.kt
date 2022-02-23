package com.example.mockwebserverexample.infra

import com.example.mockwebserverexample.infra.responses.ProductListResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<ProductListResponse>
}