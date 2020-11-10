package com.onscreen.watcher.network.model

data class ModelResponseLogin(
    var user_details: ModelResponseLoginData
)

data class ModelResponseLoginData(
    var access_token: String,
    var id: String,
    var first_name: String,
    var middleName: String,
    var last_name: String,
    var email: String,
    var profilePic: String,
    var roleId: String
)