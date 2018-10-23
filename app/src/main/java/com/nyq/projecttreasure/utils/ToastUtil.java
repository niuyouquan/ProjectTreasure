package com.nyq.projecttreasure.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nyq.projecttreasure.R;


/**
 * Created by niuyq on 2017/6/20.
 */

public class ToastUtil {

    /**
     * refreshToast:在屏幕下部显示Toast提示信息. <br/>
     *
     * @param context  -	上下文
     * @param msg      -	提示消息
     * @param lastTime -	持续时间，0-短时间，LENGTH_SHORT；1-长时间，LENGTH_LONG；
     */
    public static void showToast(Context context, String msg, int lastTime) {
        View view = View.inflate(context, R.layout.common_toast, null);
        Toast toast = new Toast(context);
        toast.setView(view);
        ((TextView) view.findViewById(R.id.common_toast_tv)).setText(msg);
        toast.setDuration(lastTime);
        toast.show();
    }

    /**
     * refreshToast:解决Toast重复显示，显示不及时. <br/>
     *
     * @param context        -	上下文
     * @param msg            -	提示消息
     * @param lastTime        -	持续时间，0-短时间，LENGTH_SHORT；1-长时间，LENGTH_LONG；
     */
    private static Toast mytoast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        @Override
        public void run() {
            mytoast.cancel();
        }
    };

    public static void refreshToast(Context context, String msg, int lastTime) {

        if (mytoast != null) {
            mHandler.removeCallbacks(r);
        }
        View view = View.inflate(context, R.layout.common_toast, null);
        TextView tvMsg = ((TextView) view.findViewById(R.id.common_toast_tv));
        if (mytoast == null) {
            mytoast = new Toast(context);
            tvMsg.setText(msg);
            mHandler.postDelayed(r, 3500);
        } else {
            tvMsg.setText(msg);
        }
        mytoast.setView(view);
        mytoast.setDuration(lastTime);
        mytoast.show();
    }
}
