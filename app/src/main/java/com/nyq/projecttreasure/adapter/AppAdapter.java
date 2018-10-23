package com.nyq.projecttreasure.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.models.AppInfo;
import com.nyq.projecttreasure.utils.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liufx on 16/4/20.
 */
public class AppAdapter extends BaseListAdapter<AppInfo> {
    protected int width = 40;
    protected int height = 40;
    private Activity activity;

    public AppAdapter(Activity context, List<AppInfo> list) {
        super(context, list);
        this.activity = context;
//        initGrid();
    }

    @Override
    public int getCount() {
        if (super.getCount() > 8) {
            return 8;
        } else {
            return super.getCount();
        }


    }

    public void initGrid() {
        /**
         * QVGA (240×320, low density, small screen) //华为C500 WQVGA (240×400,low
         * density, normal screen) FWQVGA (240×432, low density, normal screen)
         * HVGA (320×480, medium density, normal screen) WVGA800 (480×800, high
         * density, normal screen) WVGA854 (480×854 high density, normal screen)
         * //XT800
         */
        if (ScreenUtils.getEqumentWidth(activity) >= 240 && ScreenUtils.getEqumentWidth(activity) < 320) {
            width = 33;
            height = 33;
        } else if (ScreenUtils.getEqumentWidth(activity) >= 320 && ScreenUtils.getEqumentWidth(activity) < 480) {
            width = 44;
            height = 44;
        } else if (ScreenUtils.getEqumentWidth(activity) >= 480 && ScreenUtils.getEqumentWidth(activity) < 540) {
            width = 66;
            height = 66;
        } else if (ScreenUtils.getEqumentWidth(activity) >= 540 && ScreenUtils.getEqumentWidth(activity) < 720) {
            width = 80;
            height = 80;
        } else if (ScreenUtils.getEqumentWidth(activity) >= 720 && ScreenUtils.getEqumentWidth(activity) < 1080) {
            width = 88;
            height = 88;
        } else if (ScreenUtils.getEqumentWidth(activity) >= 1080) {
            width = 132;
            height = 132;
        }
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
//        ViewGroup.LayoutParams params = holder.givImage.getLayoutParams();
//        params.height = this.height;
//        params.width = this.width;
//        holder.givImage.setLayoutParams(params);
        holder.tvTitle.setText(app.getName());
        Glide.with(mContext).load(app.getImg()).into(holder.givImage);

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
