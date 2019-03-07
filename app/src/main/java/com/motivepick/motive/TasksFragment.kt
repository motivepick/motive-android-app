package com.motivepick.motive

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TasksFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        val preferences = activity!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")!!

        val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
        val subscribe: Disposable = repository.searchTasks("SESSION=" + token, false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ tasks ->
                val textView: TextView = view.findViewById(R.id.textView)
                textView.text = "There are ${tasks.size} tasks"
            }, { error ->
                Log.e("Tasks", "Error happened $error")
            })
        return view
    }

    companion object {
        fun newInstance(): TasksFragment = TasksFragment()
    }
}
