package com.onscreen.watcher.fcm.vo

import com.google.type.DateTime

data class ZoomMeetingPropertiesItem(
    var currentLayout: Int = 0,
    var isCamMuted: Boolean = false,
    var isMicMuted: Boolean = false,
    var isVolumeMuted: Boolean = false,
    var lastCheckIn: DateTime
)