package com.motivepick.motive.model

import android.content.Context
import android.net.Uri
import java.util.*

class Config(context: Context) {

    private val properties: Properties

    init {
        val stream = context.assets.open("application.properties")
        properties = Properties()
        properties.load(stream)
    }

    fun getApiUrl(): String = properties.getProperty("API_URL")

    fun getVkOauth2Url(): Uri = Uri.parse("${getApiUrl()}/oauth2/authorization/vk?mobile")

    fun getFacebookOauth2Url(): Uri = Uri.parse("${getApiUrl()}/oauth2/authorization/facebook?mobile")
}
