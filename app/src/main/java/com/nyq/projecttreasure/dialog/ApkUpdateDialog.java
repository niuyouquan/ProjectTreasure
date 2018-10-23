package com.nyq.projecttreasure.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.interfaces.Callback;


/**
 * 类名：com.gstb.agriculture.views
 * 时间：2017/11/29 10:17
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author niuyq
 */

public class ApkUpdateDialog extends Dialog{
    private Callback callback;
    private TextView mTitle;
    private TextView mContent;
    private TextView mSize;
    private TextView tvSure;
    private TextView tvCancel;

    public ApkUpdateDialog(@NonNull Context context, Callback callback) {
        super(context, R.style.CustomDialogWifi);
        this.callback = callback;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View viewroot = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_update_managerinfo, null, false);
        mTitle = (TextView) viewroot.findViewById(R.id.tv_alert_message1);
        mContent = (TextView) viewroot.findViewById(R.id.tv_alert_message2);
        mSize = (TextView) viewroot.findViewById(R.id.tv_alert_message3);
        tvSure = (TextView) viewroot.findViewById(R.id.tv_alert_sure);
        tvCancel = (TextView) viewroot.findViewById(R.id.tv_alert_cancel);
        tvSure.setText("下载");
        tvCancel.setText("取消");
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(1);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(0);
                ApkUpdateDialog.this.cancel();
            }
        });

        OnKeyListener keylistener = new OnKeyListener(){
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode== KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        setOnKeyListener(keylistener);
        setCancelable(false);
        super.setContentView(viewroot);
    }

    /**
     * 设置内容
     * @param title  标题
     * @param content  内容
     * @param fileSize  安装包大小
     * @return
     */
    public ApkUpdateDialog setContent(String title, String content, String fileSize) {
        mTitle.setText(title);
        mContent.setText(content);
        mSize.setText(fileSize);
        return this;
    }

    /**
     * 设置下载的百分比
     * @param progress  百分比
     * @return
     */
    public ApkUpdateDialog setSureText(String progress) {
        tvSure.setText(progress);
        return this;
    }

    /**
     * 对话框消失
     */
    public void dimiss() {
        ApkUpdateDialog.this.cancel();
    }

    /**
     * 点击后禁止再次点击
     */
    public void isEnabled() {
        tvSure.setClickable(false);
        tvSure.setEnabled(false);
    }

}
