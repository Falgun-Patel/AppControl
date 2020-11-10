package com.onscreen.watcher.fcm.vo

import com.google.type.DateTime

data class ZoomWelcomeProperties(
    var lastCheckIn: String ="",
    var singin_btn:Int = 0,//0 = disable, 1 click
    var joinMeeting_btn:Int = 0,//0 = disable, 1 click
    var backpress_btn: Int = 0,
    var last_event: String = ""
)