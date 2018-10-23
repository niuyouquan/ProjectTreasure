package com.nyq.projecttreasure.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by bestleiflee@outlook.com on 12/18/2014.
 */
public class ActivityManager {

    private static ActivityManager instance = new ActivityManager();
    private List<Activity> createdActivities = new ArrayList<>();

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        return instance;
    }

    public synchronized void addActivity(Activity activity) {
        if (activity != null) {
            createdActivities.add(activity);
        }
    }

    public synchronized void remove(Activity activity) {
        if (activity != null) {
            createdActivities.remove(activity);
            Log.e("ActivityMac", "removeed:" + activity.getClass().getSimpleName());
        }
    }

    public synchronized void remove(String activity) {
        for (Activity a : createdActivities) {
            if (!a.isFinishing() && a.getClass().getSimpleName().equals(activity)) {
                a.finish();
            }
        }
    }

    public synchronized int getSize() {

        return createdActivities.size();
    }

    public synchronized boolean isActivityRunning(Class<?> c) {
        for (Activity ac : createdActivities) {
            if (ac.getClass().getSimpleName().equals(c.getSimpleName()))
                return true;
        }
        return false;
    }

    public synchronized Class<?> getCurrentStackTop() {
        if (createdActivities.isEmpty())
            return null;
        return createdActivities.get(createdActivities.size() - 1).getClass();
    }

    public synchronized void finishAll() {
        for (Activity a : createdActivities) {
            if (!a.isFinishing()) {
                a.finish();
            }
        }
        createdActivities.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAll();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            LogUtil.info("ActivityManager", e);
        }
    }

    // 遍历所有Activity并finish
    public void exit() {
        if (createdActivities != null && createdActivities.size() > 0) {
            for (int i = 0; i < createdActivities.size(); i++) {
                Activity activity = (Activity) createdActivities.get(i);
                activity.finish();
            }
        }
        System.exit(0);
    }

    public synchronized Activity getCurrentActivity() {
        return createdActivities.get(createdActivities.size() - 1);
    }

    public synchronized void finsihActivity(String activityName) {
        int size = createdActivities.size();
        for (int i = 0; i < size; i++) {
            if (createdActivities.get(i).getClass().getSimpleName().equals(activityName)) {
                createdActivities.get(i).finish();
                break;
            }
        }
    }

    /**
     * 关闭指定Activity 以及其之后入栈的所有的Activity
     *
     * @param activityName
     */
    public synchronized void clearTopActivity(String activityName) {
        int size = createdActivities.size();
        boolean onTop = false;
        for (int i = 0; i < size; i++) {
            if (createdActivities.get(i).getClass().getSimpleName().equals(activityName)) {
                onTop = true;
            }
            if (onTop)
                createdActivities.get(i).finish();
        }
    }

    /**
     * 关闭指定Activity 之后入栈的所有的Activity 排除当前activity
     *
     * @param activityName
     */
    public synchronized void clearTopActivityAll(String activityName) {
        int size = createdActivities.size();
        boolean onTop = false;
        for (int i = 0; i < size - 1; i++) {
            if (createdActivities.get(i).getClass().getSimpleName().equals(activityName)) {
                onTop = true;
                continue;
            }
            if (onTop)
                createdActivities.get(i).finish();
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        for (android.app.ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
                return true;
            }
        }
        return false;
    }


}
