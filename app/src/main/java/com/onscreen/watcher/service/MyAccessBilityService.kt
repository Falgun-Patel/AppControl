package com.onscreen.watcher.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.onscreen.watcher.base.MyApplication
import com.onscreen.watcher.fcm.database.FirebaseDatabaseManager
import com.onscreen.watcher.fcm.vo.*
import com.onscreen.watcher.utils.AppConstants
import com.onscreen.watcher.utils.AppPreferences
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MyAccessBilityService : AccessibilityService() {

    @Inject
    lateinit var database: FirebaseFirestore

    @Inject
    lateinit var appPreferences: AppPreferences

    lateinit var currentNode: AccessibilityNodeInfo
    lateinit var mFirebaseDataManager: FirebaseDatabaseManager
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    var tempScreenName: String = ""


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate() {
        super.onCreate()
        MyApplication.appComponent.inject(this)

        mFirebaseDataManager = FirebaseDatabaseManager(database)


        mFirebaseDataManager.getZoomPropertiesListener(
            appPreferences
                .getAppPrefString(AppConstants.PREF_USER_device_token).toString()
        ) {

            if (it.backpress_btn != 0) {
                rootViewLayoutClick(AppConstants.PREF_ZOOM_BACK)
            }

            when (it.currentScreen) {
                "welcomeScreen" -> {

                    if (it.zoomWelcomeProperties.backpress_btn != 0) {
                        rootViewLayoutClick(AppConstants.PREF_ZOOM_BACK)
                    }

                    if (it.zoomWelcomeProperties.joinMeeting_btn != 0) {
                        rootViewBtnClick(AppConstants.PREF_ZOOM_JOIN_MEETING)
                    }

                    if (it.zoomWelcomeProperties.singin_btn != 0) {
                        rootViewBtnClick(AppConstants.PREF_ZOOM_SING_IN)
                    }
                }
                "loginScreen" -> {
                    if (it.zoomLoginProperties.email_address.isNotBlank() && it.zoomLoginProperties.email_address.isNotEmpty())
                        rootViewEditText(
                            AppConstants.PREF_ZOOM_EMAIL_ADDRESS,
                            it.zoomLoginProperties.email_address
                        )

                    if (it.zoomLoginProperties.password.isNotBlank() && it.zoomLoginProperties.password.isNotEmpty())
                        rootViewEditText(
                            AppConstants.PREF_ZOOM_PASSWORD,
                            it.zoomLoginProperties.password
                        )

                    if (it.zoomLoginProperties.singin_btn != 0)
                        rootViewBtnClick(AppConstants.PREF_ZOOM_SING_IN)

                    if (it.zoomLoginProperties.wrongcredential_ok_btn != 0) {
                        rootViewBtnClick("OK")
                    }

                    if (it.zoomLoginProperties.backpress_btn != 0) {
                        rootViewLayoutClick(AppConstants.PREF_ZOOM_BACK)
                    }


                }
                "dashBoardScreen" -> {
                    if (it.zoomDashBoard.join_button != 0) {
                        rootViewLayoutClick(AppConstants.JOIN_BUTTON)
                    }
                    if (it.zoomDashBoard.schedule_button != 0) {
                        rootViewLayoutClick(AppConstants.SCHEDULE_BUTTON)
                    }
                    if (it.zoomDashBoard.share_screen_button != 0) {
                        rootViewLayoutClick(AppConstants.SHARE_SCREEN_BUTTON)
                    }
                    if (it.zoomDashBoard.back_to_meeting_button != 0) {
                        rootViewLayoutClick(AppConstants.BACK_TO_MEETING_BUTTON)
                    }

                    if (it.zoomDashBoard.share_dialog_meeting_key.isNotBlank() && it.zoomDashBoard.share_dialog_meeting_key.isNotEmpty()){
                        rootViewEditText(
                            AppConstants.SHARING_KEY_OR_MEETING_ID,
                            it.zoomDashBoard.share_dialog_meeting_key
                        )
                    }

                    if (it.zoomDashBoard.share_dialog_ok_button != 0) {
                        rootViewLayoutClick(AppConstants.OK)
                    }

                    if (it.zoomDashBoard.new_meeting_button != 0) {
                        rootViewLayoutClick(AppConstants.NEW_MEETING_BUTTON)
                    }
                }
                "joinMeetingScreen" -> {
                    if (it.zoomJoinMeeting.backpress_btn != 0) {
                        rootViewLayoutClick(AppConstants.PREF_ZOOM_BACK)
                    }

                    if (it.zoomJoinMeeting.linkName.isNotBlank() && it.zoomJoinMeeting.linkName.isNotEmpty()) {
                        rootViewEditText(AppConstants.PREF_ZOOM_NAME, it.zoomJoinMeeting.linkName)
                    }

                    if (it.zoomJoinMeeting.meetingId.isNotBlank() && it.zoomJoinMeeting.meetingId.isNotEmpty()) {
                        rootViewEditText(
                            AppConstants.PREF_ZOOM_MEETING_ID,
                            it.zoomJoinMeeting.meetingId
                        )
                    }

                    if (it.zoomJoinMeeting.join_with_a_personal_link_name != 0) {
                        rootViewBtnClick(AppConstants.JOIN_WITH_BUTTON)
                    }

                    if (it.zoomJoinMeeting.userName.isNotBlank() && it.zoomJoinMeeting.userName.isNotEmpty()) {
                        rootViewEditText(
                            AppConstants.PREF_ZOOM_YOUR_NAME,
                            it.zoomJoinMeeting.userName
                        )
                    }

                    if (it.zoomJoinMeeting.joinMeeting_btn != 0) {
                        rootViewLayoutClick(AppConstants.JOIN_MEETING_BUTTON)
                    }

                    if (it.zoomJoinMeeting.turnOf_Audio_Switch != 0) {
                        rootViewLayoutClick(AppConstants.NO_AUDIO_CLICK, true)
                    }

                    if (it.zoomJoinMeeting.turnOf_Video_Switch != 0) {
                        rootViewLayoutClick(AppConstants.NO_VIDEO_CLICK, true)
                    }

                }
                "createNewMeetingScreen" -> {
                    if (it.zoomNewMeeting.start_a_meeing != 0) {
                        rootViewLayoutClick(AppConstants.START_A_MEETING_BUTTON, isButton = true)
                    }

                    if (it.zoomNewMeeting.video_on_switch != 0) {
                        rootViewLayoutClick(AppConstants.START_MEETING_WITH_VIDEO, true)
                    }

                    if (it.zoomNewMeeting.use_pmi_switch != 0) {
                        rootViewLayoutClick(AppConstants.START_MEETING_WITH_PMI, true)
                    }

                    if (it.zoomNewMeeting.backpress_btn != 0) {
                        rootViewLayoutClick(AppConstants.PREF_ZOOM_BACK)
                    }

                }
                "meetingScreen" -> {
                    if (it.zoomMeeting.end_call != 0) {
                        rootViewLayoutClick(AppConstants.END_CALL)
                    }

                    if (it.zoomMeeting.mute_audio != 0) {
                        rootViewLayoutClick(AppConstants.MUTE_AUDIO)
                    }

                    if (it.zoomMeeting.stop_video != 0) {
                        rootViewLayoutClick(AppConstants.STOP_VIDEO, isParentNeed = true)
                    }

                    if (it.zoomMeeting.share_button != 0) {
                        rootViewLayoutClick(AppConstants.SHARE_BUTTON)
                    }

                    if (it.zoomMeeting.participants_button != 0) {
                        rootViewLayoutClick(AppConstants.PARTICIPANTS_BUTTON)
                    }

                    if (it.zoomMeeting.audio_source_switch != 0) {
                        rootViewLayoutClick(AppConstants.AUDIO_SOURCE_BUTTON)
                    }

                    if (it.zoomMeeting.camera_switch != 0) {
                        rootViewLayoutClick(AppConstants.SWITCH_CAMERA_BUTTON)
                    }

                    if (it.zoomMeeting.leave_meeting != 0) {//dialog click
                        rootViewLayoutClick(AppConstants.LEAVE_MEETING)
                    }

                    if (it.zoomMeeting.end_meeting != 0) {//dialog click
                        rootViewLayoutClick(AppConstants.END_MEETING)
                    }

                    if (it.zoomMeeting.backpress_btn != 0) {
                        rootViewLayoutClick(AppConstants.PREF_ZOOM_BACK)
                    }
                }
            }
        }

    }

    private fun rootViewLayoutClick(
        viewName: String,
        isParentNeed: Boolean = false,
        isButton: Boolean = false
    ) {
        if (this::currentNode.isInitialized) {

            val nodeInfoList: List<AccessibilityNodeInfo> =
                currentNode.findAccessibilityNodeInfosByText(viewName)

            for (nodeInfo in nodeInfoList) {
                if (isButton) {
                    if (nodeInfo.className == "android.widget.Button") {
                        if (isParentNeed) {
                            Log.e(
                                "tagtag",
                                nodeInfo.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                    .toString()
                            )
                        } else {
                            Log.e(
                                "tagtag",
                                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                    .toString()
                            )
                        }
                    }
                } else {
                    if (isParentNeed) {
                        Log.e(
                            "tagtag",
                            nodeInfo.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                .toString()
                        )
                    } else {
                        Log.e(
                            "tagtag",
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK).toString()
                        )
                    }
                }

            }
        }
    }

    private fun rootViewBtnClick(btnName: String) {
        if (this::currentNode.isInitialized) {
            onScreen@ for (i in 0 until currentNode.childCount) {

                if (currentNode.getChild(i) != null && currentNode.getChild(i).className == "android.widget.Button"
                    && currentNode.getChild(i).text.contains(btnName)
                ) {
                    Log.e(
                        "tagtag",
                        currentNode.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            .toString()
                    )

                    break
                } else {
                    for (j in 0 until currentNode.getChild(i).childCount) {
                        // rootInActiveWindow.getChild(3)
                        if (currentNode.getChild(i) != null
                            && currentNode.getChild(i).getChild(j) != null
                            && currentNode.getChild(i)
                                .getChild(j).className == "android.widget.Button"
                            && currentNode.getChild(i).getChild(j).text.contains(btnName)
                        ) {
                            Log.e(
                                "tagtag",
                                currentNode.getChild(i).getChild(j)
                                    .performAction(AccessibilityNodeInfo.ACTION_CLICK).toString()
                            )

                            break@onScreen
                        }
                    }

                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun rootViewEditText(editTextName: String, textValue: String) {
        if (this::currentNode.isInitialized) {
            onScreenEditText@ for (i in 0 until currentNode.childCount) {
                if (currentNode.getChild(i) != null && currentNode.getChild(i).className == "android.widget.EditText" &&
                    currentNode.getChild(i).hintText == editTextName
                ) {

                    var arguments = Bundle()

                    arguments.putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        ""
                    )
                    Log.e(
                        "tagtag clean",
                        currentNode.getChild(i)
                            .performAction(AccessibilityAction.ACTION_SET_TEXT.id, arguments)
                            .toString()
                    )

                    arguments = Bundle()
                    arguments.putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        textValue
                    )
                    Log.e(
                        "tagtag",
                        currentNode.getChild(i)
                            .performAction(AccessibilityAction.ACTION_SET_TEXT.id, arguments)
                            .toString()
                    )
//                    currentNode.refresh()

                    break
                } else {
                    for (j in 0 until currentNode.getChild(i).childCount) {
                        if (currentNode.getChild(i) != null && currentNode.getChild(i)
                                .getChild(j) != null &&
                            currentNode.getChild(i)
                                .getChild(j).className == "android.widget.EditText" &&
                            currentNode.getChild(i).getChild(j).hintText == editTextName
                        ) {

                            var arguments = Bundle()
                            arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                ""
                            )
                            Log.e(
                                "tagtag clean",
                                currentNode.getChild(i).getChild(j).performAction(
                                    AccessibilityAction.ACTION_SET_TEXT.id,
                                    arguments
                                ).toString()
                            )

                            arguments = Bundle()
                            arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                textValue
                            )
                            Log.e(
                                "tagtag",
                                currentNode.getChild(i).getChild(j).performAction(
                                    AccessibilityAction.ACTION_SET_TEXT.id,
                                    arguments
                                ).toString()
                            )
                            break@onScreenEditText
                        }
                    }
                }

            }
        }
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        if (event?.packageName != null && event.className != null){

            if(event.packageName.toString() == AppConstants.PERMISSION_PACKAGE){
                permissionClick(AppConstants.ALLOW,isButton = true)
            }


            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED ||
                event.eventType == AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT
            ) {

                if (event.packageName.toString() == AppConstants.ZOOM_APP) {
                    var zoomPropertiesModel: ZoomPropertiesModel

                    currentNode = rootInActiveWindow

                    Log.e("tags", event.className.toString())

                    when (event.className) {
                        "com.zipow.videobox.WelcomeActivity" -> {//welcome screen
                            tempScreenName = "welcomeScreen"

                            zoomPropertiesModel = ZoomPropertiesModel(
                                tempScreenName,
                                zoomWelcomeProperties = ZoomWelcomeProperties(
                                    lastCheckIn = sdf.format(
                                        Date()
                                    )
                                )
                            )

                            storeDataToFSDB(zoomPropertiesModel)
                        }
                        "com.zipow.videobox.LoginActivity" -> {//login screen
                            tempScreenName = "loginScreen"

                            zoomPropertiesModel = ZoomPropertiesModel(
                                tempScreenName,
                                zoomLoginProperties = ZoomLoginProperties(
                                    lastCheckIn = sdf.format(
                                        Date()
                                    )
                                )
                            )


                            storeDataToFSDB(zoomPropertiesModel)
                        }
                        "com.zipow.videobox.JoinConfActivity" -> {//join meeting screen
                            tempScreenName = "joinMeetingScreen"

                            zoomPropertiesModel = ZoomPropertiesModel(
                                tempScreenName,
                                zoomJoinMeeting = ZoomJoinMeeting(lastCheckIn = sdf.format(Date()))
                            )

                            storeDataToFSDB(zoomPropertiesModel)
                        }
                        "com.zipow.videobox.IMActivity" -> {//dash board screen
                            tempScreenName = "dashBoardScreen"

                            zoomPropertiesModel = ZoomPropertiesModel(
                                tempScreenName,
                                zoomDashBoard = ZoomDashBoard()
                            )

                            storeDataToFSDB(zoomPropertiesModel)
                        }
                        "com.zipow.videobox.SimpleActivity" -> {//make new meeting screen
                            tempScreenName = "createNewMeetingScreen"

                            zoomPropertiesModel = ZoomPropertiesModel(
                                tempScreenName,
                                zoomNewMeeting = ZoomNewMeeting(lastCheckIn = sdf.format(Date()))
                            )

                            storeDataToFSDB(zoomPropertiesModel)
                        }
                        "com.zipow.videobox.ConfActivityNormal" -> {//meeting screen
                            tempScreenName = "meetingScreen"

                            zoomPropertiesModel = ZoomPropertiesModel(
                                tempScreenName,
                                zoomMeeting = ZoomMeeting(lastCheckIn = sdf.format(Date()))
                            )

                            storeDataToFSDB(zoomPropertiesModel)
                        }
                    }

                    if (tempScreenName.isBlank()) {
                        tempScreenName = "UnKnowScreen"
                    }
                }
            }
        }

    }

    private fun storeDataToFSDB(zoomPropertiesModel: ZoomPropertiesModel) {
        mFirebaseDataManager.addZoomProperties(
            zoomPropertiesModel,
            appPreferences.getAppPrefString(AppConstants.PREF_USER_device_token)
                .toString()
        )
        {

        }
    }

    override fun onInterrupt() {}
    override fun onServiceConnected() {
        super.onServiceConnected()
        println("onServiceConnected")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        info.packageNames = null
        serviceInfo = info
        Log.d("TAG", "onServiceConnected")
    }


    private fun permissionClick(
        viewName: String,
        isParentNeed: Boolean = false,
        isButton: Boolean = false
    ) {
        if(rootInActiveWindow != null){
            val nodeInfoList: List<AccessibilityNodeInfo> =
                rootInActiveWindow.findAccessibilityNodeInfosByText(viewName)

            for (nodeInfo in nodeInfoList) {
                if (isButton) {
                    if (nodeInfo.className == "android.widget.Button") {
                        if (isParentNeed) {
                            Log.e(
                                "tagtag",
                                nodeInfo.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                    .toString()
                            )
                        } else {
                            Log.e(
                                "tagtag",
                                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                    .toString()
                            )
                        }
                    }
                } else {
                    if (isParentNeed) {
                        Log.e(
                            "tagtag",
                            nodeInfo.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                .toString()
                        )
                    } else {
                        Log.e(
                            "tagtag",
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK).toString()
                        )
                    }
                }

            }
        }
    }

}