package com.swipe.catalog.model

import java.io.File

data class AddProductRequest(
    val productName: String,
    val productType: String,
    val price: String,
    val tax: String,
    val files: File?
)