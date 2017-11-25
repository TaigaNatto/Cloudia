package com.example.taiga.cloudia

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.ArrayAdapter

/**
 * Created by taiga on 2017/11/25.
 */
class TLPagerAdapter(fm:FragmentManager,val accessToken:String) :FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return TLFragment().newInstance("local",accessToken)
            1 -> return TLFragment().newInstance("grobal",accessToken)
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "ページ" + (position + 1)
    }
}