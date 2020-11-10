package com.onscreen.watcher.fcm.vo

data class ZoomLoginProperties(
    var lastCheckIn: String="",
    var email_address: String = "",
    var password:String ="",
    var singin_btn:Int = 0,//0 = disable, 1 click
    var wrongcredential_ok_btn:Int = 0,//0 = disable, 1 click
    var backpress_btn: Int = 0
)