package com.onscreen.watcher.fcm

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


internal class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var mCameraManager: CameraManager
    private var hasFlash: Boolean = false
    private var isFlashOn: Boolean = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)
        Log.e(
            TAG, "Notification Message Body: " + remoteMessage.notification!!.body
        )

        hasFlash = application.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        if(hasFlash){
            mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isFlashOn = !isFlashOn
                if(isFlashOn){
                    startBackgroundService()
                }else{
                    stopBackgroundService()
                }
                mCameraManager.setTorchMode(mCameraManager.cameraIdList[0],isFlashOn)
            }
        }else{
            Toast.makeText(this,"no flash",Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    fun startBackgroundService() {
//        val mIntentService =
//            Intent(applicationContext, BackGroundService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(mIntentService)
//        } else {
//            startService(mIntentService)
//        }
    }

    fun stopBackgroundService() {
//        val mIntentService =
//            Intent(applicationContext, BackGroundService::class.java)
//        stopService(mIntentService)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}