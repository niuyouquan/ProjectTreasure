package com.nyq.projecttreasure.selectphoto;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nyq.projecttreasure.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by liufx on 16/4/20.
 */
public class MorePhotoAdapter extends RecyclerView.Adapter<MorePhotoAdapter.PhotoPickViewHolder> {

    private MorePhotoAdapter.onItemListener mOnItemListener;
    private Context context;
    private LayoutInflater inflater;
    private List<String> photos = new ArrayList<>();
    public final static int TYPE_ADD = 1;
    public final static int TYPE_PHOTO = 2;
    public final static int MAX = 9;

    public MorePhotoAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void refresh(List<String> photos) {
        this.photos.clear();
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photos.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    @Override
    public PhotoPickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_ADD:
                view = inflater.inflate(R.layout.item_add, parent, false);
                break;
            case TYPE_PHOTO:
                view = inflater.inflate(R.layout.item_more_photo, parent, false);
                break;
        }
        return new PhotoPickViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoPickViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_PHOTO) {
                String photoPath = photos.get(position);
                Glide.with(context).load(photoPath).into(holder.imageView);

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemListener.onItemClick("bigImage",position);
                    }
                });

                holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        holder.delete.setVisibility(View.VISIBLE);
                        return true;
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemListener.onItemClick("delete",position);
                        holder.delete.setVisibility(View.GONE);
                    }
                });
            } else {
                holder.addTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemListener.onItemClick("add",position);
                    }
                });
            }
    }

    @Override
    public int getItemCount() {
        int count = photos.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    public interface onItemListener {
        void onItemClick(String type, int position);
    }
    public void setOnItemClickListener(onItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }


    public static class PhotoPickViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView delete;
        private TextView addTv;

        public PhotoPickViewHolder(View view) {
            super(view);
            imageView = itemView.findViewById(R.id.giv_image);
            delete = itemView.findViewById(R.id.delete);
            addTv = itemView.findViewById(R.id.addTv);
        }
    }
}
