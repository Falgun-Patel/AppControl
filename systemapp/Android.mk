LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_UNINSTALLABLE_MODULE := true
LOCAL_MODULE_PATH := $(TARGET_OUT_APPS)
LOCAL_CERTIFICATE := platform LOCAL_SRC_FILES
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_PACKAGE_NAME := MyTestApp
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_PRIVILEGED_MODULE := true
LOCAL_STATIC_JAVA_LIBRARIES := libarity android-support-v4
include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))