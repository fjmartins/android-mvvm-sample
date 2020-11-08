package com.fjmartins.forexrates.di.module

import com.fjmartins.forexrates.view.pairs.RatesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): RatesFragment
}