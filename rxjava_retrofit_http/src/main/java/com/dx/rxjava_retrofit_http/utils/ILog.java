package com.dx.rxjava_retrofit_http.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.compat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
* File: ILog.java
* Describe:
* Author: liuhuan
* Version:
* Create: 2016/5/10 15:53
*/

public class ILog
{
    public static String TAG = "com.dx.rxjava_retrofit_http";
    public static boolean DEBUG = true;

    /**
     * Customize the log tag for your application, so that other apps using
     * Volley don't mix their logs with yours. <br />
     * Enable the log property for your tag before starting your app: <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag,boolean isDebug) {
        if(TextUtils.isEmpty(tag)){
            i("Changing log tag to " + tag);
            TAG = tag;
        }
        // Reinitialize the DEBUG "constant"
//        DEBUG = Log.isLoggable(TAG, Log.VERBOSE);
        DEBUG = isDebug;
    }

    @SuppressLint("LongLogTag")
    public static void v(Object msg) {
        if (DEBUG)
            Log.v(TAG, buildMessage(String.valueOf(msg)));
    }

    @SuppressLint("LongLogTag")
    public static void i(Object msg) {
        if (DEBUG)
            Log.i(TAG, buildMessage(String.valueOf(msg)));
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }


    public static void iw(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    @SuppressLint("LongLogTag")
    public static void w(Object msg) {
        if (DEBUG)
            Log.w(TAG, buildMessage(String.valueOf(msg)));
    }


    @SuppressLint("LongLogTag")
    public static void d(Object msg) {
        if (DEBUG)
            Log.d(TAG, buildMessage(String.valueOf(msg)));
    }

    @SuppressLint("LongLogTag")
    public static void e(Object msg) {
        if (DEBUG)
            Log.e(TAG, buildMessage(String.valueOf(msg)));
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void ew(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }
    @SuppressLint("LongLogTag")
    public static void e(Object msg, Context context) {
        if (DEBUG)
            Log.e(TAG, buildMessage(String.valueOf(msg)));
    }

    @SuppressLint("LongLogTag")
    public static void e(Throwable tr, Object msg) {
        if (DEBUG)
            Log.e(TAG, buildMessage(String.valueOf(msg)), tr);
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        StringBuilder caller = new StringBuilder();
        // Walk up the stack looking for the first caller outside of ILog.
        // It will be at least two frames up, so start there.
        try {
            for (int i = 2; i < trace.length; i++) {
                Class<?> clazz = trace[i].getClass();
                if (!clazz.equals(ILog.class)) {
                    String callingClass = getSimpleClassName(trace[i].getClassName());
                    caller.append(callingClass).append('.').append(trace[i].getMethodName()).append(':')
                            .append(trace[i].getLineNumber());
                    break;
                }
            }
        } catch (Exception e) {
        }
        return String.format(Locale.US, "<%s> msg: %s", caller, msg);
    }

    public static String getSimpleClassName(String path) throws Exception {
        int index = path.lastIndexOf('.');
        path = index == -1 ? path : path.substring(index + 1);
        index = path.lastIndexOf('$');
        path = index == -1 ? path : path.substring(0, index);
        return path;
    }

}

