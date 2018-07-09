package com.daimao.bluebubble;


import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;


public class BaseApplication extends Application {

    private final static String TAG = BaseApplication.class.getCanonicalName();

    private static BaseApplication self;

    private Handler mHandler;

    private Toast mToast;

    private int mActivityResumedCount = 0;

    private static final long CHECK_DELAY = 500;

    private NotificationManager mNotificationManager; //通知服务

    public static List<Activity> activities = new ArrayList<>();

    public static String downloadPath = Environment.getExternalStorageDirectory()
            + File.separator + "BlueBubble"+ File.separator + "download";

    public static String logoImgPath = "/logo";

    public static String tmpPath = "/tmp";

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        logoImgPath = getDiskCacheDir(getAppContext()) + File.separator + "logo";

        tmpPath =  getDiskCacheDir(getAppContext()) + File.separator + "tmp";

        mHandler = new Handler();

        this.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }


    public static BaseApplication getInstance() {
        return self;
    }

    public static Context getAppContext() {
        if (self != null) {
            return self.getApplicationContext();
        }
        return null;
    }

    public  void showTip(String text, boolean showLong){
        if(mToast != null){
            mToast.cancel();
        }
        if(showLong){
            mToast = Toast.makeText(getAppContext(), "", Toast.LENGTH_LONG);
        }else{
            mToast = Toast.makeText(getAppContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
        Log.i(LOG_TAG, "showTip()");
    }

    public  void showTip(String text){
        showTip(text, false);
    }

    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.v(TAG, activity.getLocalClassName() + ".onActivityCreated()");
            activities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.v(TAG, activity.getLocalClassName() + ".onActivityStarted()");
        }

        @Override
        public void onActivityResumed(Activity activity) {

            Log.v(TAG, activity.getLocalClassName() + ".onActivityResumed()");
            mActivityResumedCount++;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //发送通知
//                    if (isScreenOn() && !isBackground()) {
//                        mApplicationBroadcastSender.onBecameForeground();
//                    }
                }
            }, CHECK_DELAY);

        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.v(TAG, activity.getLocalClassName() + ".onActivityPaused()");
            mActivityResumedCount--;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //发送通知
//                    if (isScreenOn() && isBackground()) {
//                        mApplicationBroadcastSender.onBecameBackground();
//                    }
                }
            }, CHECK_DELAY);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.v(TAG, activity.getLocalClassName() + ".onActivityStopped()");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            Log.v(TAG, activity.getLocalClassName() + ".onActivitySaveInstanceState()");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.v(TAG, activity.getLocalClassName() + ".onActivityDestroyed()");
            activities.remove(activity);
        }
    };

    public boolean isBackground() {
        if (mActivityResumedCount > 0) {
            Log.i("BlueBubbleApp", "前台运行");
            return false;
        } else {
            Log.i("BlueBubbleApp", "后台运行");
            return true;
        }
    }

    public void finishAll(){
        for (Activity activity : activities){
            activity.finish();
        }
    }

}
