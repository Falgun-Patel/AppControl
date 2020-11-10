package com.onscreen.watcher.fcm.vo

data class ZoomPropertiesModel(
    var currentScreen: String = "",
    var isCallRunning: Boolean = false,
    var backpress_btn: Int = 0,
    var is_tv_on_off: Int = 0,
    var fireCommand: String= "",
    var commnadResult: String = "",
    var zoomWelcomeProperties: ZoomWelcomeProperties = ZoomWelcomeProperties(),
    var zoomLoginProperties: ZoomLoginProperties = ZoomLoginProperties(),
    var zoomJoinMeeting: ZoomJoinMeeting = ZoomJoinMeeting(),
    var zoomDashBoard: ZoomDashBoard = ZoomDashBoard(),
    var zoomNewMeeting: ZoomNewMeeting = ZoomNewMeeting(),
    var zoomMeeting: ZoomMeeting = ZoomMeeting()
) {}