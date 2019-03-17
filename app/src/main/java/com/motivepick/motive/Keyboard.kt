package com.motivepick.motive

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

object Keyboard {

    fun enterPressed(actionId: Int, event: KeyEvent): Boolean =
        actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.action == KeyEvent.ACTION_DOWN)
}
