package com.swipe.catalog.network

import com.swipe.catalog.model.AddProductResponse
import com.swipe.catalog.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductApiService {
    @GET("get")
    suspend fun getProducts(): List<Product>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: MultipartBody.Part?,
    ): AddProductResponse
}
