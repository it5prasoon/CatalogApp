package com.swipe.catalog.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swipe.catalog.model.AddProductRequest
import com.swipe.catalog.network.ApiResponse
import com.swipe.catalog.network.ProductApiService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddProductViewModel(private val productApiService: ProductApiService) : ViewModel() {

    private val _addProductResult = MutableLiveData<ApiResponse<Unit>>()
    val addProductResult: LiveData<ApiResponse<Unit>> get() = _addProductResult

    fun addProduct(product: AddProductRequest) {
        product.files?.let { Log.i("Logs - Add Product", it.name) }
        viewModelScope.launch {
            try {
                _addProductResult.value = ApiResponse.Loading
                val response = productApiService.addProduct(
                    product.productName.toRequestBody(),
                    product.productType.toRequestBody(),
                    product.price.toString().toRequestBody(),
                    product.tax.toString().toRequestBody(),
                    product.files?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it) }?.let {
                        MultipartBody.Part.createFormData(
                            "files[]",
                            product.files?.name,
                            it
                        )
                    }
                )
                Log.i("Logs - Add Product", response.message)
                _addProductResult.value = ApiResponse.Success(Unit)
            } catch (e: Exception) {
                _addProductResult.value = ApiResponse.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}