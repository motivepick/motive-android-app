package com.motivemobileapp.schedule

import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

object ScheduleUiState {

    private var state: Parcelable? = null

    fun save(recyclerView: RecyclerView) {
        state = (recyclerView.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    fun restore(recyclerView: RecyclerView) = (recyclerView.layoutManager as LinearLayoutManager).onRestoreInstanceState(state)
}
