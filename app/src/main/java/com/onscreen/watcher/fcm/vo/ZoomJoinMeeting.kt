package com.onscreen.watcher.fcm.vo

data class ZoomJoinMeeting (
    var lastCheckIn: String ="",
    var meetingId:String="",//edit text
    var userName:String="",//edit text
    var linkName:String="",//edit text
    var join_with_a_personal_link_name:Int = 0,//using this you can join zoom with id or link name
    var joinMeeting_btn:Int = 0,//0 = disable, 1 click
    var turnOf_Audio_Switch:Int = 0,//0 = false, 1 true
    var turnOf_Video_Switch:Int = 0,//0 = false, 1 true
    var backpress_btn: Int = 0
)