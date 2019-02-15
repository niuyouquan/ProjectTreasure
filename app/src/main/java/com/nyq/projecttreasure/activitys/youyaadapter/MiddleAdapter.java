package com.nyq.projecttreasure.activitys.youyaadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liufx.extras.span.SpanAdapterDelegate;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.TimeHelper;
import com.nyq.projecttreasure.views.MLImageView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @package: com.nyq.projecttreasure.activitys.youyaadapter
 * @author: niuyq
 * @date: 2019/2/14
 * Copyright © 2019 某某某公司. All rights reserved.
 * @description: <rec>
 */

public class MiddleAdapter extends SpanAdapterDelegate<HealthInfo, MiddleAdapter.ViewHolder> {
    private Context context;

    public MiddleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_middle_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, HealthInfo healthInfo) {
        super.onBindViewHolder(holder, position, healthInfo);
        holder.itemTv1.setText(healthInfo.getTitle());
        holder.msgTime.setText(TimeHelper.formatStringToDate(new Date().toString(), "yyyy-M-d"));
        holder.source.setText(StringHelper.isBlank(healthInfo.getSource()) ? "未知" : healthInfo.getSource());
        Glide.with(context).load(healthInfo.getImg()).into((MLImageView) holder.itemImage);
    }

    @Override
    public void onItemClick(View view, HealthInfo item, int position) {
        super.onItemClick(view, item, position);
        if (onItemClickListerner != null) {
            onItemClickListerner.onItemClick(view, item, position);
        }

    }

    //监听事件
    private OnItemClickListerner onItemClickListerner;

    public interface OnItemClickListerner {
        void onItemClick(View view, HealthInfo item, int position);
    }

    public OnItemClickListerner getOnItemClickListerner() {
        return onItemClickListerner;
    }

    public void setOnItemClickListerner(OnItemClickListerner onItemClickListerner) {
        this.onItemClickListerner = onItemClickListerner;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        MLImageView itemImage;
        @BindView(R.id.item_tv1)
        TextView itemTv1;
        @BindView(R.id.source)
        TextView source;
        @BindView(R.id.msg_time)
        TextView msgTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
