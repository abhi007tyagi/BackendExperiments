//
// Created by Abhinav Tyagi on 31/03/16.
//

#include <jni.h>

#include <string.h>
#include <stdlib.h>

extern "C"
jstring
Java_com_tyagiabhinav_einvite_Invite_getKey( JNIEnv* env,jobject thiz,jstring rootPath )
{
    const char *s = env->GetStringUTFChars(rootPath, 0);
    env->ReleaseStringUTFChars(rootPath, s);
    return env->NewStringUTF("aBh!n@v7yA6i");
}