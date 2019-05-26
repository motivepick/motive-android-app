package com.motivemobileapp.common

import java.util.*

class CurrentDateFactoryImpl : CurrentDateFactory {

    override fun now(): Date {
        return Date()
    }
}
