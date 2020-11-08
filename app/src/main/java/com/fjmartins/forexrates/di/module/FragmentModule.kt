package com.fjmartins.forexrates.di.module

import com.fjmartins.forexrates.view.currencies.CurrencyFragment
import com.fjmartins.forexrates.view.rates.RatesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCurrencyFragment(): CurrencyFragment

    @ContributesAndroidInjector
    abstract fun contributeRatesFragment(): RatesFragment

}