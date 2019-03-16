package com.motivepick.motive

import android.net.Uri

class Token(private val value: String) {

    constructor(uri: Uri?) : this(uri.toString().replace("motive://", "").replace("#_=_", ""))

    fun isBlank(): Boolean = value.isBlank()

    fun toCookie(): String = "SESSION=$value"

    override fun toString(): String = value
}
