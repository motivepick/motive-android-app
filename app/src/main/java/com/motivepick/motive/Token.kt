package com.motivepick.motive

import android.net.Uri

class Token(private val uri: Uri?) {

    override fun toString(): String = uri.toString().replace("motive://", "").replace("#_=_", "")
}
