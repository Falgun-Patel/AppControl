//package com.onscreen.watcher.service
//
//import android.accessibilityservice.AccessibilityService
//import android.accessibilityservice.AccessibilityServiceInfo
//import android.app.*
//import android.app.ActivityManager.RunningAppProcessInfo
//import android.content.Context
//import android.content.Intent
//import android.content.pm.ApplicationInfo
//import android.content.pm.PackageManager
//import android.content.pm.ServiceInfo
//import android.graphics.BitmapFactory
//import android.os.Build
//import android.os.IBinder
//import android.util.Log
//import android.view.accessibility.AccessibilityManager
//import androidx.core.app.NotificationCompat
//import com.onscreen.watcher.R
//import com.onscreen.watcher.base.MyApplication
//import com.onscreen.watcher.fcm.database.FirebaseDatabaseManager
//import com.onscreen.watcher.fcm.vo.FirebaseModel
//import java.util.*
//import javax.inject.Inject
//
//
//class BackGroundService : Service() {
//
//    @Inject
//    lateinit var mContext: Context
//
//
//    lateinit var fm: FirebaseDatabaseManager
//
//    private var notificationChannelId = "com.onscreen.watcher"
//    private var channelName = "OnScreen call"
//    private var chan: NotificationChannel? = null
//    private var manager: NotificationManager? = null
//    private var notificationBuilder: NotificationCompat.Builder? = null
//
//    private lateinit var mTimer: Unit
//
//
//    init {
//        MyApplication.appComponent.inject(this)
//
//        fm = FirebaseDatabaseManager(database)
//    }
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        super.onStartCommand(intent, flags, startId)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startMyOwnForeground()
//        }
//
//        return START_STICKY
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
////        startScanning()
//
//
//        }
//    }
//
//    private fun startScanning() {
//        Timer().scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                val enabled = isAccessibilityServiceEnabled(
//                    MyService::class.java
//                )
//                if (!enabled) {
//                    /*Settings.Secure.putString(
//                        contentResolver,
//                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "com.onscreen.watcher/MyService")
//                    Settings.Secure.putString(
//                        contentResolver,
//                        Settings.Secure.ACCESSIBILITY_ENABLED, "1")*/
//                }
//                Log.e("xxxxxxxxxxxx", enabled.toString())
//            }
//
//        }, 0, 1000)
//
//    }
//
//    fun isAccessibilityServiceEnabled(
//        service: Class<out AccessibilityService?>
//    ): Boolean {
//        val am =
//            this.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
//        val enabledServices =
//            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
//        for (enabledService in enabledServices) {
//            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
//
//            if (enabledServiceInfo.packageName == this.packageName && enabledServiceInfo.name == service.name
//            ) return true
//        }
//        return false
//    }
//
////    private fun enableAccessibilityService() {
////        val packageName = "com.onscreen.watcher"
////        val className = "$packageName.service.MyService"
////        val string = "enabled_accessibility_services"
////        val cmd = "settings put secure $string $packageName/$className"
////        InstrumentationRegistry.getInstrumentation()
////            .getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
////            .executeShellCommand(cmd)
////            .close()
////        TimeUnit.SECONDS.sleep(3)
////    }
//
//
//    fun isForeground(ctx: Context, myPackage: String): Boolean {
//        val manager =
//            ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val runningTaskInfo = manager.getRunningTasks(1)
//        val componentInfo = runningTaskInfo[0].topActivity
//        return componentInfo!!.packageName == myPackage
//    }
//
//
//    private fun startMyOwnForeground() {
//        manager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            chan = NotificationChannel(
//                notificationChannelId,
//                channelName,
//                NotificationManager.IMPORTANCE_NONE
//            )
//            chan!!.setShowBadge(false)
//            chan!!.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//            manager!!.createNotificationChannel(chan!!)
//        }
//        notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
//        notificationBuilder!!.setContentTitle("Welcome to OnScreen")
//            .setStyle(NotificationCompat.BigTextStyle().bigText("Tap to Open Home Screen"))
//            .setPriority(NotificationManager.IMPORTANCE_MIN)
//            .setCategory(Notification.CATEGORY_SERVICE)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder!!.setSmallIcon(R.drawable.ic_home_active)
//            notificationBuilder!!.setLargeIcon(
//                BitmapFactory.decodeResource(
//                    resources,
//                    R.drawable.ic_home_active
//                )
//            )
//        } else {
//            notificationBuilder!!.setSmallIcon(R.drawable.ic_home_active)
//        }
//
//        val notification: Notification = notificationBuilder!!.setOngoing(true).build()
//        startForeground(2, notification)
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        val broadcastIntent = Intent("ac.in.ActivityRecognition.RestartSensor")
//        sendBroadcast(broadcastIntent)
//    }
//
//    fun getActiveApps(context: Context): String? {
//
//        val activityManager =
//            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val recentTasks =
//            Objects.requireNonNull(activityManager).getRunningTasks(Int.MAX_VALUE)
//        for (i in recentTasks.indices) {
//            print(
//                """Application executed: ${recentTasks[i].baseActivity!!.toShortString()}ID: ${recentTasks[i].id}"""
//            )
//        }
//
//
//        val mngr =
//            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//
//        val taskList = mngr.getRunningTasks(10)
//
//        if (taskList[0].numActivities == 1 && taskList[0].topActivity!!.className == this.javaClass.name) {
//            print("parth This is last activity in the stack")
//        }
//
//
//        val am =
//            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val tasks = am.getRunningTasks(1)
//        val task = tasks[0] // current task
//
//        val rootActivity = task.baseActivity
//
//
//        val currentPackageName = rootActivity!!.packageName
//        if (currentPackageName.contains("olx")) {
//            print("parth")
//        }
//
//
//        val l =
//            am.getRecentTasks(1, ActivityManager.RECENT_WITH_EXCLUDED)
//        val i = l.iterator()
//        val pmmm = this.packageManager
//        while (i.hasNext()) {
//            val info = i.next() as RunningAppProcessInfo
//            try {
//                val c = pmmm.getApplicationLabel(
//                    pmmm.getApplicationInfo(
//                        info.processName, PackageManager.GET_META_DATA
//                    )
//                )
//                print("parth LABEL$c");
//            } catch (e: Exception) {
//                // Name Not FOund Exception
//            }
//        }
//
//
//        val pm = context.packageManager
//        val packages =
//            pm.getInstalledApplications(PackageManager.GET_META_DATA)
//        var value: String = "parth"// basic date stamp
//        value += "---------------------------------\n"
//        value += "Active Apps\n"
//        value += "=================================\n"
//        for (packageInfo in packages) {
//
//            //system apps! get out
//            if (!isSTOPPED(packageInfo) && !isSYSTEM(packageInfo) && packageInfo.packageName.contains(
//                    "olx"
//                )/*&& isForeground(mContext,packageInfo.packageName)*/) {
//                value += getApplicationLabel(
//                    context,
//                    packageInfo.packageName
//                ).toString() + "\n" + packageInfo.packageName + "\n-----------------------\n"
//            }
//        }
//        return value
//
//    }
//
//
//    private fun isSTOPPED(pkgInfo: ApplicationInfo): Boolean {
//        return pkgInfo.flags and ApplicationInfo.FLAG_STOPPED != 0
//    }
//
//    private fun isSYSTEM(pkgInfo: ApplicationInfo): Boolean {
//        return pkgInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
//    }
//
//
//    fun getApplicationLabel(
//        context: Context,
//        packageName: String
//    ): String? {
//        val packageManager = context.packageManager
//        val packages =
//            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
//        var label: String? = null
//        for (i in packages.indices) {
//            val temp = packages[i]
//            if (temp.packageName == packageName) label =
//                packageManager.getApplicationLabel(temp).toString()
//        }
//        return label
//    }
//
//}