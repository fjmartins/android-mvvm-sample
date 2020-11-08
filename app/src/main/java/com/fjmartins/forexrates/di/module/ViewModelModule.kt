package com.fjmartins.forexrates.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fjmartins.forexrates.di.annotation.ViewModelKey
import com.fjmartins.forexrates.di.factory.ViewModelFactory
import com.fjmartins.forexrates.view.rates.RatesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindPairsViewModel(pairsViewModel: RatesViewModel): ViewModel
}