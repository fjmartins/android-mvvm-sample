package com.fjmartins.forexrates.view

import android.os.Bundle
import com.fjmartins.forexrates.R
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.view.base.BaseActivity
import com.fjmartins.forexrates.view.currencies.SelectCurrencyFragment
import dagger.android.AndroidInjection

class MainActivity : BaseActivity(), Injectable {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SelectCurrencyFragment.newInstance())
                    .commitNow()
        }
    }
}