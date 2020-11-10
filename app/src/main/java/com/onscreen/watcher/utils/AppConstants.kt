package com.onscreen.watcher.utils

class AppConstants {

    companion object {
        const val BASE_URL: String = "http://oneclickitmarketing.co.in:5056/"
        const val PREFS_NAME: String = "OnScreen_Call_Preferences"


        const val PREF_USER_device_token: String = "device_token"
        const val PREF_ZOOM_COLLECTION: String = "zoom_pro"

        //app names
        const val ZOOM_APP = "us.zoom.videomeetings"

        //permission handle
        const val PERMISSION_PACKAGE = "com.google.android.packageinstaller"
        const val DENY = "DENY"
        const val ALLOW = "ALLOW"
        const val PERMISSOIN_MIC = "Allow Zoom to"//Allow Zoom to record audio
        const val PERMISSION_CAMERA = "Allow Zoom to"//Allow Zoom to take pictures and record video

        //zoom welcome screen
        const val PREF_ZOOM_SING_UP: String = "Sign Up"
        const val PREF_ZOOM_SING_IN: String = "Sign In"
        const val PREF_ZOOM_JOIN_MEETING: String = "Join a Meeting"
        const val PREF_ZOOM_BACK: String = "Cancel"

        //zoom sing in screen
        const val PREF_ZOOM_EMAIL_ADDRESS: String = "Email Address"// edit text
        const val PREF_ZOOM_PASSWORD: String = "Password"// edit text

        //zoom join meeting screen
        const val PREF_ZOOM_MEETING_ID: String = "Meeting ID"// edit text
                            //or
        const val PREF_ZOOM_NAME: String = "Personal Link Name"// edit text

        const val PREF_ZOOM_YOUR_NAME: String = "Your Name"// edit text
        const val JOIN_MEETING_BUTTON = "Join Meeting"//button
        const val NO_AUDIO_CLICK = "Don't Connect To Audio" //switch
        const val NO_VIDEO_CLICK = "Turn off My Video" //switch
        const val JOIN_WITH_BUTTON = "Join with" //its multi text button


        //zoom dashboard screen
        const val NEW_MEETING_BUTTON = "New Meeting, button"
        const val JOIN_BUTTON = "Join, button"
        const val SCHEDULE_BUTTON = "Schedule, button"
        const val SHARE_SCREEN_BUTTON = "Share Screen, button"
        const val BACK_TO_MEETING_BUTTON = "Back to meeting, button"

        //dashboardscreen share dialog
        const val OK = "OK"
        const val SHARING_KEY_OR_MEETING_ID = "Sharing key or Meeting ID"

        //zoom meeting screen
        const val END_CALL = "End"
        const val END_MEETING = "End Meeting"
        const val LEAVE_MEETING ="Leave Meeting"

        const val MUTE_AUDIO = "Mute"//mute my audio
        const val STOP_VIDEO = "Stop Video"//stop my video
        const val SHARE_BUTTON = "Share, button"
        const val PARTICIPANTS_BUTTON = "Participants"
        const val AUDIO_SOURCE_BUTTON = "Audio Source"//boolean out put audio ex:- speaker or earphone
        const val SWITCH_CAMERA_BUTTON = "Switch Camera"//boolean Switch Camera, front camera selected , Switch Camera, back camera selected

        //zoom meeting screen share dialog fileds
//        const val microsoft OneDrive
//        const val google Drive
//        const val box
//        const val photo
//        const val document
//        const val web URL
//        const val bookmark
//        const val screen
//        const val share Whiteboard

        //zoom start new meeting
        const val START_A_MEETING_BUTTON ="Start a Meeting"//find by text and filter with android.widget.Button
        const val START_MEETING_WITH_VIDEO = "Video On"//boolean find by text and click on parent
        const val START_MEETING_WITH_PMI = "Use Personal"//boolean find by text and click on parent
    }
}