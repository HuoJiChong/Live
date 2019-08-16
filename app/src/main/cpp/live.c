#include "com_derek_live_JniPush_Pusher.h"

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    startPush
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_startPush
        (JNIEnv * env, jobject obj, jstring jstr_url){

}

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    stopPush
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_stopPush
        (JNIEnv * env, jobject obj){

}

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    release
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_release
        (JNIEnv * env, jobject obj){

}

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    setVideoOptions
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_setVideoOptions
        (JNIEnv * env, jobject obj, jint jwidth, jint jheight, jint jbitrate, jint jfps){

}

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    setAudioOptions
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_setAudioOptions
        (JNIEnv * env, jobject jobj, jint jsampleRateInHz, jint jchannel){

}

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    fireVideo
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_fireVideo
        (JNIEnv * env, jobject obj, jbyteArray jvideoDataArray){

}

/*
 * Class:     com_derek_live_JniPush_Pusher
 * Method:    fireAudio
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_com_derek_live_JniPush_Pusher_fireAudio
        (JNIEnv * env, jobject obj, jbyteArray jaudioDataArray, jint len){

}



