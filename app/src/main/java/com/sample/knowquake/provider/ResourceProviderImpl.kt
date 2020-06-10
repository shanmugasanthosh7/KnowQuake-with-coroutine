package com.sample.knowquake.provider

import android.content.Context
import androidx.annotation.StringRes

class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}