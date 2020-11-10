package com.onscreen.watcher.fcm.vo

data class ZoomNewMeeting (
    var lastCheckIn: String ="",
    var start_a_meeing:Int = 0,//0 = disable, 1 click
    var video_on_switch:Int = 0,//0 = false, 1 true
    var use_pmi_switch:Int = 0,//0 = false, 1 true
    var backpress_btn: Int = 0
)