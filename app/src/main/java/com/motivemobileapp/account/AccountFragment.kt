package com.motivemobileapp.account

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.motivemobileapp.LoginActivity
import com.motivemobileapp.R
import com.motivemobileapp.TokenStorage

class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            TokenStorage(activity).removeToken()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }
        return view
    }

    companion object {
        fun newInstance(): AccountFragment = AccountFragment()
    }
}