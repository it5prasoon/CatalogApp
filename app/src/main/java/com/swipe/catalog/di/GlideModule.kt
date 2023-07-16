package com.swipe.catalog.di

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.module.AppGlideModule

class GlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Customize Glide options if needed
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register custom Glide components if needed
    }

    companion object {
        @JvmStatic
        fun create(application: Application): GlideModule = GlideModule()
    }
}
