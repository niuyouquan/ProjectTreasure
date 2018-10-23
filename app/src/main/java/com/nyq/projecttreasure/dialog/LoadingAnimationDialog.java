package com.nyq.projecttreasure.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyq.projecttreasure.R;


/**
 * 类名: LoadingAnimationDialog <br/>
 * 功能: 自定义进度提示控件. <br/> 
 * 创建时间: 2018.9.25 <br/>
 * @author niuyq
 * @version
 */
public class LoadingAnimationDialog extends Dialog {

    @SuppressWarnings("unused")
	private Context mContext = null;

    private static LoadingAnimationDialog loadingAnimationDialog = null;

    public LoadingAnimationDialog(Context context){
        super(context);
        this.mContext = context;
    }

    public LoadingAnimationDialog(Context context, int theme) {
        super(context, R.style.LoadingAnimationDialog);
        this.mContext = context;
    }

    public static LoadingAnimationDialog show(Context context){
		loadingAnimationDialog = new LoadingAnimationDialog(context, R.style.LoadingAnimationDialog);
		loadingAnimationDialog.setContentView(R.layout.common_loading_dialog);
		loadingAnimationDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		loadingAnimationDialog.show();
        return loadingAnimationDialog;
    }
    
    
    public static Dialog show(Context context,String title,String msg){
    	
    	LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_vartical, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_loading);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if (msg!=null) {
			tipTextView.setText(msg);// 设置加载信息
		}
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_vartical);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

		loadingDialog.show();
    	return loadingDialog;

    }
    
    public static Dialog show(Context context, String title, String msg, boolean cancelable){
    	
    	LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_vartical, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_loading);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if (msg!=null) {
			tipTextView.setText(msg);// 设置加载信息
		}
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_vartical);// 创建自定义样式dialog

		loadingDialog.setCancelable(cancelable);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

		loadingDialog.show();
    	return loadingDialog;
    }
    public static Dialog horizontalShow(Context context,String title,String msg,boolean cancelable){
    	
    	LayoutInflater inflater = LayoutInflater.from(context);
    	View v = inflater.inflate(R.layout.dialog_horizontal, null);// 得到加载view
    	LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
    	// main.xml中的ImageView
    	ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_loading);
    	TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
    	// 加载动画
    	Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
    			context, R.anim.loading);
    	// 使用ImageView显示动画
    	spaceshipImage.startAnimation(hyperspaceJumpAnimation);
    	if (msg!=null) {
    		tipTextView.setText(msg);// 设置加载信息
    	}
    	Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_horizontal);// 创建自定义样式dialog
    	
    	loadingDialog.setCancelable(cancelable);// 不可以用“返回键”取消
    	loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

    	loadingDialog.show();
    	return loadingDialog;
    }
     public static  LoadingAnimationDialog show1(Context context,String title,String msg,boolean cancelable) {


		 loadingAnimationDialog = new LoadingAnimationDialog(context,R.style.LoadingAnimationDialog);
		 loadingAnimationDialog.setContentView(R.layout.common_loading_dialog);
		 loadingAnimationDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		 loadingAnimationDialog.setCancelable(cancelable);
     	TextView tvMsg = (TextView)loadingAnimationDialog.findViewById(R.id.common_loading_dialog_tv);
         if (tvMsg != null){
             tvMsg.setText(msg);
         }
		 loadingAnimationDialog.show();
		return loadingAnimationDialog;
		
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
    	 if (loadingAnimationDialog == null){
    		 return;
    	 }
         ImageView imageView = (ImageView) loadingAnimationDialog.findViewById(R.id.common_loading_dialog_iv);
         Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this.getContext(),R.anim.loading);
         imageView.startAnimation(hyperspaceJumpAnimation);
	}

    /**
     * setMessage 提示内容
     * @param strMessage
     * @return
     */
    public LoadingAnimationDialog setMessage(String strMessage){
        TextView tvMsg = (TextView)loadingAnimationDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }
        return loadingAnimationDialog;
    }
}
