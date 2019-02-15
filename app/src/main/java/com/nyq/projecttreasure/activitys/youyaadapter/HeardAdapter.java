package com.nyq.projecttreasure.activitys.youyaadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liufx.extras.span.SpanAdapterDelegate;
import com.nyq.projecttreasure.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @package: com.nyq.projecttreasure.activitys.youyaadapter
 * @author: niuyq
 * @date: 2019/2/14
 * Copyright © 2019 某某某公司. All rights reserved.
 * @description: <rec>
 */

public class HeardAdapter extends SpanAdapterDelegate<PeopleInfoListVo, HeardAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_heard_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, PeopleInfoListVo item) {
        super.onBindViewHolder(holder, position, item);
        holder.tvCjr.setText(item.getUserName());
        holder.tvWcsj.setText(item.getFinishTime());
        holder.tvXxcj.setText(item.getScore() + "");
    }

    @Override
    public void onItemClick(View view, PeopleInfoListVo item, int position) {
        super.onItemClick(view, item, position);
        if (onItemClickListerner != null) {
            onItemClickListerner.onItemClick(view, item, position);
        }

    }

    //监听事件
    private OnItemClickListerner onItemClickListerner;

    public interface OnItemClickListerner {
        void onItemClick(View view, PeopleInfoListVo item, int position);
    }

    public OnItemClickListerner getOnItemClickListerner() {
        return onItemClickListerner;
    }

    public void setOnItemClickListerner(OnItemClickListerner onItemClickListerner) {
        this.onItemClickListerner = onItemClickListerner;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_cjr)
        TextView tvCjr;
        @BindView(R.id.tv_wcsj)
        TextView tvWcsj;
        @BindView(R.id.tv_xxcj)
        TextView tvXxcj;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
