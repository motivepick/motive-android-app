package com.motivepick.motive

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val editor: SharedPreferences.Editor = activity!!.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            editor.remove("token")
            editor.apply()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        return view
    }

    companion object {
        fun newInstance(): AccountFragment = AccountFragment()
    }
}
