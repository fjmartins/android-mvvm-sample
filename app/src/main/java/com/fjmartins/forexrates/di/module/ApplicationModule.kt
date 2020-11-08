package com.fjmartins.forexrates.di.module

import androidx.room.Room
import com.fjmartins.forexrates.ForexApplication
import com.fjmartins.forexrates.dao.ForexDatabase
import com.fjmartins.forexrates.network.CurrencyLayerApi
import com.fjmartins.forexrates.repository.ForexRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, RetrofitModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideForexDatabase(app: ForexApplication): ForexDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            ForexDatabase::class.java, "forex-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideForexRepository(database: ForexDatabase, currencyLayerApi: CurrencyLayerApi): ForexRepository {
        return ForexRepository(database, currencyLayerApi)
    }
}