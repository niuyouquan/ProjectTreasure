/*
 Copyright © 2015, 2016 Jenly Yu <a href="mailto:jenly1314@gmail.com">Jenly</a>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package com.nyq.projecttreasure.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nyq.projecttreasure.fragments.JkzxFragment;
import com.nyq.projecttreasure.models.InfoColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用ViewPagerFragmentAdapter
 * @author liufx
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> listData;

    private List<InfoColumn> list;


    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.list = new ArrayList<>();
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> listData){
        this(fm,listData,null);
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> listData, List<InfoColumn> list) {
        super(fm);
        this.listData = listData;
        this.list = list;
    }

    public List<Fragment> getListData() {
        return listData;
    }

    public void setListData(List<Fragment> listData) {
        this.listData = listData;
    }

    public List<InfoColumn> getList() {
        return list;
    }

    public void setList(List<InfoColumn> list) {
//        this.list = list;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
//        return listData==null ? null : listData.get(position) ;
        JkzxFragment fragment = new JkzxFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", list.get(position).getColumnName());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
//        return list.get(position).getColumnName();
        String plateName = list.get(position).getColumnName();
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
