package com.nyq.projecttreasure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础adapter
 *
 * @param <E>
 */
public abstract class BaseListAdapter<E> extends BaseAdapter {

    protected Context mContext;
    private List<E> mList = new ArrayList<E>();
    protected LayoutInflater mInflater;

    public BaseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public BaseListAdapter(Context context, List<E> list) {
        this(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void clearAll() {
        mList.clear();
    }

    public List<E> getData() {
        return mList;
    }

    public void addALL(List<E> list) {
        if (list == null || list.isEmpty())
            return;
        mList.addAll(list);
    }

    public void add(E item) {
        mList.add(item);
    }

    @Override
    public E getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeEntity(E e) {
        mList.remove(e);
    }

}
