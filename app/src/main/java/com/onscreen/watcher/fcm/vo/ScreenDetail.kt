package com.onscreen.watcher.fcm.vo

import com.google.type.DateTime

data class CallScreenDetail(
    var isCamMuted: Boolean = false,
    var isMicMuted: Boolean = false,
    var isVolumeMuted: Boolean = false,
    var lastCheckIn: DateTime
)
