package com.nyq.projecttreasure.popupwindow;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nyq.projecttreasure.R;

/**
 * Created by niuyq on 2018/12/25.
 */

public class AddressPopup extends PopupWindow {
    private View mMenuView;
    private Button btn_ok;
    private Button btn_cancel;
    private TextView asName;


    public AddressPopup(Context context, String addressName) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_address, null);

        asName = mMenuView.findViewById(R.id.tv_address_name);
        btn_ok = mMenuView.findViewById(R.id.btn_ok);
        btn_cancel = mMenuView.findViewById(R.id.btn_cancel);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        this.setAnimationStyle(R.style.BottomPopAnim);
//        设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        实例化一个ColorDrawable颜色为全透明！！！为了和window底色融合，设置#00ffffff
         ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        asName.setText(addressName);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
