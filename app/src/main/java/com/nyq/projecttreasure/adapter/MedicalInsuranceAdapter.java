package com.nyq.projecttreasure.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.ListBaseAdapter;
import com.nyq.projecttreasure.base.SuperViewHolder;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.TimeHelper;
import com.nyq.projecttreasure.views.MLImageView;

import java.util.Date;


/**
 * Created by niuyq on 2018/11/12.
 * 医保报销列表适配器
 */

public class MedicalInsuranceAdapter extends ListBaseAdapter<HealthInfo> {

    private Context context;

    public MedicalInsuranceAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_listview_jkzx;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        HealthInfo bean = mDataList.get(position);

        TextView tvName = holder.getView(R.id.item_tv1);//调查员姓名
        TextView tvIdCard = holder.getView(R.id.msg_time);//手机号
        TextView tvAddress = holder.getView(R.id.source);//地址

        tvName.setText(bean.getTitle() + "");
        tvIdCard.setText(TimeHelper.formatStringToDate(new Date().toString(), "yyyy-M-d"));
        tvAddress.setText(StringHelper.isBlank(bean.getSource()) ? "未知" : bean.getSource());
        Glide.with(mContext).load(bean.getImg()).into((MLImageView) holder.getView(R.id.item_image));
    }
}
