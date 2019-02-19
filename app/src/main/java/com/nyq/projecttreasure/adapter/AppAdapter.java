package com.nyq.projecttreasure.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.models.AppInfo;
import com.nyq.projecttreasure.utils.ImageLoaderUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liufx on 16/4/20.
 */
public class AppAdapter extends BaseListAdapter<AppInfo> {
    private Activity activity;

    public AppAdapter(Activity context, List<AppInfo> list) {
        super(context, list);
        this.activity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_app, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo app = getItem(position);
        holder.tvTitle.setText(app.getName());
        ImageLoaderUtils.loadImageViewLoding(mContext,app.getImg(),holder.givImage,R.mipmap.ic_launcher,R.mipmap.ic_launcher);

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.giv_image)
        ImageView givImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
