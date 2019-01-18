package com.mrrun.project.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

/**
 * 应用的Activity生命周期管理
 * 1、Android应用前后台的判断
 * 2、监测所有Activity生命周期事件代码
 * 3、优雅的退出应用
 * 4、对ActivityLifecycleCallbacks接口的封装，封装应用Activity生命周期管理类
 *
 * @author lipin
 * @version 1.0
 * @date 2019/01/17
 */
public class ActivityLifecycleManager implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ActivityLifecycleManager.class.getSimpleName();

    private static volatile ActivityLifecycleManager instance;

    private Application application;

    /**
     * 保存应用内所有未销毁的Activity(包括第三方的)
     */
    private Stack<Activity> activityStack = new Stack<>();

    /**
     * 应用前台标识
     */
    private boolean foreground = false;

    private int count = 0;

    private ActivityLifecycleManager() {
    }

    /**
     * @return an initialised ActivityLifecycleManager instance
     */
    public static ActivityLifecycleManager getInstance() {
        Log.i(TAG, "ActivityLifecycleManager getInstance");
        if (null == instance) {
            synchronized (ActivityLifecycleManager.class) {
                if (null == instance) {
                    instance = new ActivityLifecycleManager();
                }
            }
        }
        return instance;
    }

    /**
     * @param app
     */
    public void init(Application app) {
        Log.i(TAG, "ActivityLifecycleManager init");
        application = app;
        if (null == instance) {
            instance = getInstance();
        }
        application.registerActivityLifecycleCallbacks(instance);
        return;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "ActivityLifecycleManager onActivityCreated->" + activity);
        if (null != activityStack){
            activityStack.push(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "ActivityLifecycleManager onActivityStarted->" + activity);
        if (count == 0) {
            foreground = true;
            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
        }
        count++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "ActivityLifecycleManager onActivityResumed->" + activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i(TAG, "ActivityLifecycleManager onActivityPaused->" + activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(TAG, "ActivityLifecycleManager onActivityStopped->" + activity);
        count--;
        if (count == 0) {
            foreground = false;
            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.i(TAG, "ActivityLifecycleManager onActivitySaveInstanceState->" + activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG, "ActivityLifecycleManager onActivityDestroyed->" + activity);
        // 这里不用activityStack.pop()的原因是，执行pop时是取出栈顶元素，并将该元素从栈中删除，可能销毁的不是同一个入栈Activity
        if (null != activityStack){
            activityStack.remove(activity);
        }
    }

    /**
     * 额外的注册应用Activity生命周期回调(非必须)
     *
     * @param callback
     */
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        if (null == application) {
            throw new NullPointerException("application can't be null");
        }
        application.registerActivityLifecycleCallbacks(callback);
    }

    /**
     * 应用是否处于前台
     *
     * @return
     */
    public boolean isForeground() {
        return foreground;
    }

    /**
     * 应用是否处于后台
     *
     * @return
     */
    public boolean isBackground() {
        return !foreground;
    }

    /**
     * 优雅的退出应用
     */
    public void exitAppGrace() {
        if (null != activityStack && !activityStack.empty()) {
            for (Activity activity : activityStack) {
                activity.finish();
            }
            activityStack.clear();
        }
    }
}
