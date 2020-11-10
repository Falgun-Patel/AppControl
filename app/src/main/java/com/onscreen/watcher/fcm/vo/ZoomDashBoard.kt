package com.onscreen.watcher.fcm.vo

data class ZoomDashBoard(
    var new_meeting_button: Int = 0,
    var join_button: Int = 0,
    var schedule_button: Int = 0,
    var share_screen_button: Int = 0,
    var share_dialog_meeting_key:String = "",
    var share_dialog_ok_button: Int = 0,
    var back_to_meeting_button: Int = 0
) {}