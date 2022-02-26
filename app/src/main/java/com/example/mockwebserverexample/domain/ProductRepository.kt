package com.example.mockwebserverexample.domain

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun createProduct(product: Product): Int
    suspend fun deleteProduct(product: Product): Boolean
}