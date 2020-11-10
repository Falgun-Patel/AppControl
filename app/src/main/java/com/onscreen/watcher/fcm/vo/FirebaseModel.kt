
package com.onscreen.watcher.fcm.vo

data class FirebaseModelResponse(val id: String = "",
                        val isRunning: Boolean = false)


fun FirebaseModelResponse.isValid() = id.isNotBlank()

fun FirebaseModelResponse.mapToJoke() = FirebaseModel(id, isRunning)

data class FirebaseModel(val id: String,
                         val isRunning: Boolean= false)