package com.motivemobileapp

import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

object UiState {

    private var state: MutableMap<String, Parcelable?> = mutableMapOf()

    fun save(view: String, recyclerView: RecyclerView) {
        state[view] = (recyclerView.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    fun restore(view: String, recyclerView: RecyclerView) = (recyclerView.layoutManager as LinearLayoutManager).onRestoreInstanceState(state[view])
}
