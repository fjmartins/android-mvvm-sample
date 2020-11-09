package com.fjmartins.forexrates.view

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fjmartins.forexrates.R
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.model.ConversionHelper
import com.fjmartins.forexrates.view.base.BaseActivity
import dagger.android.AndroidInjection

class MainActivity : BaseActivity(), Injectable {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // setting title according to fragment
        navController.addOnDestinationChangedListener { _, destination, arguments ->

            setToolbarTitle(destination.label, false)

            when (destination.id) {
                R.id.ratesFragment -> {
                    val conversionHelper = arguments?.get("conversionHelper") as ConversionHelper
                    setToolbarTitle(
                        getString(R.string.rates_for).format(
                            conversionHelper.amount,
                            conversionHelper.currency
                        ), true
                    )
                }
            }
        }
    }

    private fun setToolbarTitle(title: CharSequence?, displayHomeUp: Boolean) {
        supportActionBar?.run {
            this.title = title
            this.setHomeButtonEnabled(displayHomeUp)
            this.setDisplayHomeAsUpEnabled(displayHomeUp)
        }
    }

    override fun onSupportNavigateUp(): Boolean = navController.popBackStack()
}