package com.nyq.projecttreasure.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.setting.WholePatternCheckingActivity;
import com.nyq.projecttreasure.activitys.setting.WholePatternSettingActivity;
import com.nyq.projecttreasure.activitys.start.LoginActivity;
import com.nyq.projecttreasure.utils.PatternHelper;
import com.nyq.projecttreasure.utils.SPUtils;
import com.nyq.projecttreasure.utils.StringHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.panpf.switchbutton.SwitchButton;


/**
 * 我
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userContent)
    TextView userContent;
    @BindView(R.id.profile)
    LinearLayout profile;
    @BindView(R.id.szssmm)
    TextView szssmm;
    @BindView(R.id.bzfk)
    TextView bzfk;
    @BindView(R.id.yssm)
    TextView yssm;
    @BindView(R.id.gywm)
    TextView gywm;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.switchBtn)
    SwitchButton switchBtn;

    private SharedPreferences sp;
    private PatternHelper patternHelper;
    private String phString;
    private boolean isCheckeds = false;

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    public MeFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        patternHelper = new PatternHelper();
        phString = patternHelper.getFromStorage();
        isCheckeds = (boolean) SPUtils.get(getContext(),"isChecked",false);
        switchBtn.setChecked(isCheckeds);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (StringHelper.isNotBlank(phString)) {
                        SPUtils.put(getContext(),"isChecked",true);
                    } else {
                        SPUtils.put(getContext(),"isChecked",false);
                    }
                    szssmm.setTextColor(getResources().getColor(R.color.text_black));
                }else {
                    SPUtils.put(getContext(),"isChecked",false);
                    szssmm.setTextColor(getResources().getColor(R.color.font_color));
                }
            }
        });

        szssmm.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.szssmm:
                if (switchBtn.isChecked()){
                    if (StringHelper.isNotBlank(phString)) {
                        intent.setClass(getContext(),WholePatternCheckingActivity.class);
                    } else {
                        intent.setClass(getContext(),WholePatternSettingActivity.class);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.btn_logout:
                new PatternHelper().clearStorage();
                patternHelper = new PatternHelper();
                phString = patternHelper.getFromStorage();
                SPUtils.put(getContext(),"isChecked",false);
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
