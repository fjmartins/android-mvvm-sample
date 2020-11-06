package com.fjmartins.forexrates.di.component

import com.fjmartins.forexrates.ForexApplication
import com.fjmartins.forexrates.di.module.ActivityModule
import com.fjmartins.forexrates.di.module.ApplicationModule
import com.fjmartins.forexrates.di.module.RetrofitModule
import com.fjmartins.forexrates.di.module.ViewModelModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: ForexApplication): Builder

        fun build(): AppComponent
    }

    fun inject(forexApplication: ForexApplication)
}