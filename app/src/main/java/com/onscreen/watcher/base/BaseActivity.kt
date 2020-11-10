package com.onscreen.watcher.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.onscreen.watcher.R
import com.onscreen.watcher.databinding.BaseActivityBinding
import com.onscreen.watcher.utils.AppPreferences
import com.onscreen.watcher.utils.AppUtils
import com.onscreen.watcher.utils.listeners.ConnectivityReceiver
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var appUtils: AppUtils

    @Inject
    lateinit var appPreferences: AppPreferences

    lateinit var baseViewModel: BaseViewModel

//    private lateinit var baseActivityBinding: BaseActivityBinding


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        MyApplication.appComponent.inject(this)

        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel::class.java)

    }

    protected open fun <T : ViewDataBinding?> putContentView(@LayoutRes resId: Int): T {
        val frameLayout = findViewById<ViewGroup?>(R.id.base_activity_fl_content)

        return DataBindingUtil.inflate<T>(
            layoutInflater,
            resId,
            frameLayout,
            true
        )
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        MyApplication.isInternetAvailable = isConnected
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}