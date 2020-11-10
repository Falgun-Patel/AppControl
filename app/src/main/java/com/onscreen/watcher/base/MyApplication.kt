package com.onscreen.watcher.base

import android.app.Application
import androidx.multidex.MultiDex
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.onscreen.watcher.di.component.AppComponent
import com.onscreen.watcher.di.component.DaggerAppComponent
import com.onscreen.watcher.di.modules.AppModule
import com.onscreen.watcher.di.modules.FirebaseModule
import com.onscreen.watcher.di.modules.NetworkModule
import com.onscreen.watcher.fcm.database.FirebaseDatabaseManager
import com.onscreen.watcher.fcm.vo.ZoomMeeting
import com.onscreen.watcher.fcm.vo.ZoomPropertiesModel
import com.onscreen.watcher.utils.AppConstants
import com.onscreen.watcher.utils.AppPreferences
import com.onscreen.watcher.utils.hdmi.UsbService
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject


class MyApplication : Application() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var database: FirebaseFirestore

    lateinit var mFirebaseDataManager: FirebaseDatabaseManager
    private val currentPacket: ArrayList<Byte> = ArrayList()


    companion object {
        lateinit var application: MyApplication
        lateinit var appComponent: AppComponent
        var isInternetAvailable: Boolean = false
        lateinit var usbService: UsbService
        private const val MSG_START = 255.toByte()
        private const val MSG_END = 254.toByte()

    }


    override fun onCreate() {
        super.onCreate()
        application = this

        appComponent = DaggerAppComponent.builder()
            .netWorkModule(NetworkModule)
            .appModule(AppModule(application))
            .firebaseModule(FirebaseModule())
            .build()

        appComponent.inject(this)

        FirebaseApp.initializeApp(this)

        MultiDex.install(this)

        getFirebaseToken()

        mFirebaseDataManager = FirebaseDatabaseManager(database)
    }

    private fun listenFirebaseProperties() {

        mFirebaseDataManager.getZoomPropertiesListener(
            appPreferences
                .getAppPrefString(AppConstants.PREF_USER_device_token).toString()
        ) {

//            if (it.is_tv_on_off != 0) {
//
//            }

            if (it.is_tv_on_off.toString()[0].toInt() == 1) {
                fireCommand("echo 0x40 0x04")//tv on

            } else if (it.is_tv_on_off.toString()[0].toInt() == 0) {
                fireCommand("echo 0x40 0x36")//tv off

            } else if (it.fireCommand.isNotBlank() && it.fireCommand.isNotEmpty()) {
                fireCommand(it.fireCommand)
            }

        }
    }

    private fun fireCommand(fireCommand: String) {

        try {
            val su = Runtime.getRuntime().exec(fireCommand)
            su.waitFor()
            printResults(su)
        } catch (e: IOException) {
            throw Exception(e)
        } catch (e: InterruptedException) {
            throw Exception(e)
        }
    }

    @Throws(IOException::class)
    fun printResults(process: Process) {
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String? = ""
        while (reader.readLine().also { line = it } != null) {
            addPropertiesTofirebase(line.toString())
            println("xxxxxxx")
            println(line)
        }
    }

    private fun addPropertiesTofirebase(value: String = "") {
        mFirebaseDataManager.addZoomProperties(
            ZoomPropertiesModel(
                "tempScreenName",
                zoomMeeting = ZoomMeeting(),
                commnadResult = value
            ),
            appPreferences.getAppPrefString(AppConstants.PREF_USER_device_token)
                .toString()
        )
        {
            print(it)
        }
    }

    private fun getFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                if (instanceIdResult != null) {
                    appPreferences.writeSharedPreferencesString(
                        AppConstants.PREF_USER_device_token,
                        instanceIdResult.token
                    )

                    addPropertiesTofirebase()
                    listenFirebaseProperties()
                }
            }
    }
}