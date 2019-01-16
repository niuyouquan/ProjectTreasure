package com.nyq.projecttreasure.popupwindow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.popupwindow.base.BasePopup;

/**
 * Created by niuyq on 2018/12/26.
 */

public class MyPopupWindow extends BasePopup<MyPopupWindow> {

    private TextView tvAddressName;
    private Button btnCancel;
    private Button btnOk;
    private String addressName;

    private MyPopupWindow(Context context, String addressName) {
        setContext(context);
        this.addressName = addressName;
    }

    public static MyPopupWindow create(Context context,String addressName) {
        return new MyPopupWindow(context, addressName);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.popup_address);
        setFocusAndOutsideEnable(false)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);
    }

    @Override
    protected void initViews(View view, MyPopupWindow popup) {
        btnOk = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_cancel);
        tvAddressName = findViewById(R.id.tv_address_name);

        tvAddressName.setText(addressName);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
