package com.onscreen.watcher.di.component

import com.onscreen.watcher.base.BaseActivity
import com.onscreen.watcher.base.BaseViewModel
import com.onscreen.watcher.base.MyApplication
import com.onscreen.watcher.di.modules.AppModule
import com.onscreen.watcher.di.modules.FirebaseModule
import com.onscreen.watcher.di.modules.NetworkModule
import com.onscreen.watcher.service.MyAccessBilityService
import com.onscreen.watcher.utils.AppPreferences
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class, FirebaseModule::class])
interface AppComponent {

    fun inject(myApplication: MyApplication)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseViewModel: BaseViewModel)
    fun inject(appPreferences: AppPreferences)
//    fun inject(backGroundService: BackGroundService)
    fun inject(myService: MyAccessBilityService)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun netWorkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
        fun firebaseModule(firebaseModule: FirebaseModule): Builder
    }
}