package com.motivepick.motive.common

import java.util.*

class CurrentDateFactoryImpl : CurrentDateFactory {

    override fun now(): Date {
        return Date()
    }
}
