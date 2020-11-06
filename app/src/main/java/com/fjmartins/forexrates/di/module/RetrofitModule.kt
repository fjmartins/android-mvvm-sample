package com.fjmartins.forexrates.di.module

import com.fjmartins.forexrates.network.CurrencyLayerApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient.Builder {
        return OkHttpClient.Builder() //必要に応じてここでInterceptし、AuthorizationのBearerトークンを設定
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient.Builder) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(CurrencyLayerApi.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyLayerApi(retrofit: Retrofit) : CurrencyLayerApi {
        return retrofit.create(CurrencyLayerApi::class.java)
    }
}