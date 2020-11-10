package com.onscreen.watcher.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onscreen.watcher.network.PostApi
import com.onscreen.watcher.utils.AppPreferences
import io.reactivex.disposables.Disposable
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject
    lateinit var postApi: PostApi

    @Inject
    lateinit var context: Application

    @Inject
    lateinit var appPreferences: AppPreferences

    //Disposables
    private lateinit var subscription: Disposable

    //Progress
    val isShowProgress = MutableLiveData<Boolean>(false)
    var strErrorBase = MutableLiveData<String>("")

    init {
        inject()
    }

    private fun inject() {
        MyApplication.appComponent.inject(this)
    }
}