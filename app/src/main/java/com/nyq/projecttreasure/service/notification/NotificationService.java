package com.nyq.projecttreasure.service.notification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nyq.projecttreasure.utils.Constant;

/**
 * Created by niuyq on 2018/12/4.
 */

public class NotificationService extends Service{
    public static final int TYPE_HANGUP = 6;
    private MusicBroadReceiver receiver;
    private NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        regFilter();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.e("MusicService","MusicService");
    }

    /**
     * 注册广播
     */
    private void regFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CLOSE);
        intentFilter.setPriority(1000);
        if (receiver == null) {
            receiver = new MusicBroadReceiver();
        }
        getApplicationContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 广播接收者
     */
    public class MusicBroadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.ACTION_CLOSE:
                    notificationManager.cancel(TYPE_HANGUP);
                    stopSelf();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getApplicationContext().unregisterReceiver(receiver);
        }
        stopSelf();
    }
}
