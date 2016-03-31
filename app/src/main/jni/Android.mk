LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := keys
LOCAL_SRC_FILES := keygen.cpp
LOCAL_STATIC_LIBRARIES := boost_serialization_static
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)