package com.example.mockwebserverexample.infra

import com.example.mockwebserverexample.domain.Product
import com.example.mockwebserverexample.domain.ProductRepository

class ProductRepositoryImpl(
    private val service: ProductService
): ProductRepository{

    override suspend fun getProducts(): List<Product> {
        val response = service.getProducts().body()
        val products = response?.products ?: listOf()
        return products.map {
            it.toProduct()
        }
    }

    override suspend fun createProduct(product: Product): Int {
        return 0
    }

    override suspend fun deleteProduct(product: Product): Boolean {
        TODO("Not yet implemented")
    }


}