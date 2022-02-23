package com.example.mockwebserverexample.infra.responses

import com.google.gson.annotations.SerializedName

data class ProductListResponse (
    @SerializedName("results")
    val products: List<ProductResponse>
)