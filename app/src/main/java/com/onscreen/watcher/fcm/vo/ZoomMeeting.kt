package com.onscreen.watcher.fcm.vo

data class ZoomMeeting(
    var lastCheckIn: String ="",
    var end_call:Int=0,
    var mute_audio:Int=0,
    var stop_video:Int=0,
    var share_button:Int = 0,
    var participants_button:Int = 0,
    var audio_source_switch:Int = 0,//false = speaker, true = earphone
    var camera_switch:Int = 0,//false = rear camera, true = front camera

    var end_meeting:Int = 0,//end call dialog click
    var leave_meeting:Int =0,//end call dialog click

    var backpress_btn: Int = 0
)