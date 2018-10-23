package com.nyq.projecttreasure.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyq.projecttreasure.R;


/**
 * @desc:(通用对话框)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:2017/5/15 10:27
 */

public class CustomDialog extends Dialog {

    private static boolean cancelable = true;

    public CustomDialog(Context context) {
        super(context);
        setCancelable(cancelable);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        setCancelable(cancelable);
    }

    public static class Builder {
        private Context context;
        private String title;//消息标题
        private String message;//消息内容
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean viImag;
        private int positiveButtonColor;
        private int negativeButtonColor;
        private int imgSrc = R.mipmap.hint;//中间内容图片
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setImagVisible(boolean viImag) {
            this.viImag = viImag;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCancel() {
            cancelable = false;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setImgSrc(int imgSrc) {
            this.imgSrc = imgSrc;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, int positiveButtonColor, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonColor = positiveButtonColor;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, int positiveButtonColor, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonColor = positiveButtonColor;
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButton(int negativeButtonText, int negativeButtonColor, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonColor = negativeButtonColor;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, int negativeButtonColor, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonColor = negativeButtonColor;
            this.negativeButtonClickListener = listener;
            return this;
        }


        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_custom, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title

            // set the confirm button
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                ((TextView) layout.findViewById(R.id.positiveButton)).setTextColor(ContextCompat.getColor(context, positiveButtonColor));
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.positiveButton)
                            .setOnClickListener(v -> positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE));
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
                layout.findViewById(R.id.divder).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                ((TextView) layout.findViewById(R.id.negativeButton)).setTextColor(ContextCompat.getColor(context, negativeButtonColor));
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.negativeButton)
                            .setOnClickListener(v -> negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE));
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
                layout.findViewById(R.id.divder).setVisibility(View.GONE);
            }
            if (imgSrc != 0) {//设置内容图片
                layout.findViewById(R.id.content_img).setBackgroundResource(imgSrc);
            } else {
                layout.findViewById(R.id.content_img).setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }

            if (title != null) {
                layout.findViewById(R.id.title).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }

            if (viImag) {
                layout.findViewById(R.id.vd).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
            } else {
                layout.findViewById(R.id.iv_right).setVisibility(View.GONE);
            }

            dialog.setContentView(layout);

            Window window = dialog.getWindow();
            WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (d.getWidth() * 0.75); // 宽度设置为屏幕的0.75
            window.setAttributes(p);

            return dialog;
        }

    }

}
