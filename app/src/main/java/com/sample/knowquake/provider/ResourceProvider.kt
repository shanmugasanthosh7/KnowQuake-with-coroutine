package com.sample.knowquake.provider

import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes id: Int): String

}