package com.mrrun.project.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    protected ActivityLifecycleManager lifecycleManager = ActivityLifecycleManager.getInstance();

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        lifecycleManager.init(this);
    }

    public void registerActivityLifecycleCallback(){
        // 这个方法调用是非必须的
        lifecycleManager.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivityCreated: " + activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivityStarted: " + activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivityResumed: " + activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivityPaused: " + activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivityStopped: " + activity);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivitySaveInstanceState: " + activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i(TAG, "ActivityLifecycleCallbacks->onActivityDestroyed: " + activity);
            }
        });
    }
}
