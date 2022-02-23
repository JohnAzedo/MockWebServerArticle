package com.example.mockwebserverexample.domain

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    fun createProduct(product: Product): Int
}