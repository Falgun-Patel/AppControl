package com.onscreen.watcher.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.onscreen.watcher.utils.AppPreferences
import com.onscreen.watcher.utils.AppUtils
import com.onscreen.watcher.R
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class AppModule(private val application: Application) {

    @Provides
    @Reusable
    internal fun providerApplication(): Application {
        return application
    }

    @Provides
    @Reusable
    internal fun providerContext(): Context {
        return application
    }

    @Provides
    @Reusable
    internal fun providesSharedPrefereces(): SharedPreferences {
        return application.getSharedPreferences(
            application.resources.getString(R.string.app_name),
            0
        )
    }

    @Provides
    @Reusable
    internal fun providesSharedPreferencesEditor(): SharedPreferences.Editor {
        return providesSharedPrefereces().edit()
    }

    @Provides
    @Reusable
    internal fun providerUtils(): AppUtils {
        return AppUtils.newInstance(application)
    }

    @Provides
    @Reusable
    internal fun providerPreferences(): AppPreferences {
        return AppPreferences.newInstance(application)
    }

}