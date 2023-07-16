package com.swipe.catalog.model

import com.google.gson.annotations.SerializedName

/**
 * Schema for Product Details
 * @param image
 * @param price
 * @param productName
 * @param productType
 * @param tax
 */
data class Product(
    val image: String?,
    val price: String,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_type")
    val productType: String,
    val tax: String
)
