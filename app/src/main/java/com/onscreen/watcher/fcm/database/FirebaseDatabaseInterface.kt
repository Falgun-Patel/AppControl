
package com.onscreen.watcher.fcm.database

import com.onscreen.watcher.fcm.vo.ZoomPropertiesModel

interface FirebaseDatabaseInterface {

  fun addZoomProperties(zoomProperties: ZoomPropertiesModel, token:String, onResult: (Boolean) -> Unit)

  fun getZoomPropertiesListener(token:String, onResult: (ZoomPropertiesModel) -> Unit)

}