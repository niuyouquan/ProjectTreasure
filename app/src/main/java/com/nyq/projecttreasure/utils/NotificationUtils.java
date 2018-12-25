package com.nyq.projecttreasure.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.notification.NotificationActivity;
import com.nyq.projecttreasure.service.notification.NotificationService;


/**
 * 通知
 * Created by niuyq on 2018/12/3.
 */

public class NotificationUtils {

    public static final int TYPE_NORMAL = 1;  // 普通通知
    public static final int TYPE_PROGRESS = 2;  // 下载进度的通知
    public static final int TYPE_BIG_TEXT = 3;  // BigTextStyle通知
    public static final int TYPE_INBOX = 4;  // InboxStyle
    public static final int TYPE_BIG_PICTURE = 5;  // BigPictureStyle
    public static final int TYPE_HANGUP = 6;  // hangup横幅通知
    private static NotificationUtils notificationUtils = null;
    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    /**
     * 单例模式中获取唯一的notificationUtils实例
     *
     * @return
     */
    public static NotificationUtils getInstance(Context context) {
        if (null == notificationUtils) {
            notificationUtils = new NotificationUtils(context);
        }
        return notificationUtils;
    }

    /**
     * 自定义通知
     *
     * @param ticker
     * @param contentTitle
     * @param contentText
     */
    public void showNotification(String ticker, String contentTitle, String contentText) {
        Notification notification;
        //获取通知管理器
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent it = new Intent(context, NotificationActivity.class);
        //(Context对象，Intent的id，Intent对象，对Intent的更新方式)
        //FLAG_UPDATE_CURRENT表示不销毁原来的PendingIntent，直接替换其中的Intent内容
        //FLAG_CANCEL_CURRENT表示取消当前的PendingIntent，重新创建新的PendingIntent对象
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_CANCEL_CURRENT);
        //创建一个远程视图管理
        RemoteViews contentView = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.notification_layout);
        contentView.setTextViewText(R.id.n_title, contentTitle);
        contentView.setTextViewText(R.id.n_content, contentText);//设置文本框中显示的文本内容
        contentView.setImageViewResource(R.id.n_icon, R.mipmap.image_avatar_1);//设置图片视图

        //设置自定义布局中的点击监听
        //关闭的Intent,MusicService 进入onStartCommand中
        Intent closeIntent = new Intent(context, NotificationService.class);
        closeIntent.setAction(Constant.ACTION_CLOSE);
        PendingIntent closePdIntent = PendingIntent.getService(context, 2, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.n_close, closePdIntent);

        if (Build.VERSION.SDK_INT >= 16) {
            Notification.Builder builder = new Notification.Builder(context);
            //显示在状态栏上的小图标
            builder.setSmallIcon(R.drawable.icon);
            //显示在状态栏上的提示文本(显示一段时间消失)
            builder.setTicker(ticker);
//            //下拉看到的内容标题和文本
//            builder.setContentTitle(contentTitle);
//            builder.setContentText(contentText);
//            //通知时间
//            builder.setWhen(System.currentTimeMillis());
            builder.setContent(contentView);
            //如果点击了内容部分，跳转到Activity01界面
            builder.setContentIntent(pIntent);
            //创建通知
            notification = builder.build();
        } else {
            notification = new Notification(R.drawable.icon, ticker, System.currentTimeMillis());
            notification.contentIntent = pIntent;
            notification.contentView = contentView;
        }
        //自动取消(当通知被点击时,系统会移除通知)
        notification.flags = notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        // 显示通知
        nm.notify(TYPE_HANGUP, notification);
    }

    public void ptNotification() {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Ticker是状态栏显示的提示
        builder.setTicker("简单Notification");
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle("标题");
        //第二行内容 通常是通知正文
        builder.setContentText("通知内容");
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        builder.setSubText("这里显示的是通知第三行内容！");
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
        builder.setNumber(2);
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.image_avatar_1);
        //下拉显示的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_avatar_1));
        Intent intent = new Intent(context, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent, 0);
        //点击跳转的intent
        builder.setContentIntent(pIntent);
        //通知默认的声音 震动 呼吸灯
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manager.notify(TYPE_NORMAL, notification);
    }
}
