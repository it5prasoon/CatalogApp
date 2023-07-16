package com.swipe.catalog.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.swipe.catalog.model.Product
import com.swipe.catalog.network.ApiResponse
import com.swipe.catalog.network.ProductApiService
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productApiService: ProductApiService,
    private val glide: RequestManager
) : ViewModel() {
    private val _products = MutableLiveData<ApiResponse<List<Product>>>()
    val products: LiveData<ApiResponse<List<Product>>> get() = _products

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                _products.value = ApiResponse.loading()
                val response = productApiService.getProducts()
                Log.i("Logs - Fetch Products", response.toString())
                _products.value = ApiResponse.success(response)
            } catch (e: Exception) {
                _products.value = ApiResponse.error(e.message ?: "Unknown error occurred")
            }
        }
    }

}
