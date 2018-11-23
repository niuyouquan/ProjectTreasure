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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用ViewPagerFragmentAdapter
 * @author liufx
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<InfoColumn> list;

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.list = new ArrayList<>();
        this.fragmentList =  new ArrayList<>();
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<InfoColumn> list) {
        super(fm);
        this.fragmentList = fragmentList;
        this.list = list;
    }

    public void setListData(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    public List<Fragment> getListData() {
        return fragmentList;
    }

    public void setList(List<InfoColumn> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<InfoColumn> getList() {
        return list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
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
