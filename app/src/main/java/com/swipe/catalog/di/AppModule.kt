package com.swipe.catalog.di

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.swipe.catalog.network.ProductApiService
import com.swipe.catalog.viewmodels.AddProductViewModel
import com.swipe.catalog.viewmodels.HomeViewModel
import com.swipe.catalog.adapters.ProductAdapter
import com.swipe.catalog.utils.Constants.SWIPE_API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Module for initiating single instance of dependencies
 */
val appModule = module {
    single {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(SWIPE_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ProductApiService::class.java)
    }

    single<RequestManager> {
        Glide.with(androidContext())
    }

    viewModel {
        HomeViewModel(get(), get())
    }

    single {
        GlideModule.create(get())
    }

    factory { (glide: RequestManager) ->
        ProductAdapter(glide)
    }

    viewModel {
        AddProductViewModel(get())
    }
}
