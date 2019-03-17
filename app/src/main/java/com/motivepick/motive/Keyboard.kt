package com.motivepick.motive

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

object Keyboard {

    fun enterPressed(actionId: Int, event: KeyEvent?): Boolean =
        actionId == EditorInfo.IME_ACTION_DONE || enterPressedOnSimulator(actionId, event)

    /**
     * If the application is running on a simulator and you press Enter on a physical keyboard,
     * that should be detected in a slightly different way.
     */
    private fun enterPressedOnSimulator(actionId: Int, event: KeyEvent?): Boolean =
        actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event?.action == KeyEvent.ACTION_DOWN
}
