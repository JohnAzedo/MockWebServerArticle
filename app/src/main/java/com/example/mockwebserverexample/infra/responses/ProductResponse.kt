package com.example.mockwebserverexample.infra.responses

import com.example.mockwebserverexample.domain.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("quantity")
    val quantity: Int
){
    fun toProduct() = Product(
        id = id,
        name = name,
        price = price,
        quantity = quantity
    )
}


