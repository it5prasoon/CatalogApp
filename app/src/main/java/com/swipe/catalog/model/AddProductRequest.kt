package com.swipe.catalog.model

import java.io.File

data class AddProductRequest(
    val productName: String,
    val productType: String,
    val price: Double,
    val tax: Double,
    val files: File?
)