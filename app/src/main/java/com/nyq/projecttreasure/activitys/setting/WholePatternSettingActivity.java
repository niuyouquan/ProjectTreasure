package com.nyq.projecttreasure.activitys.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ihsg.patternlocker.IHitCellView;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternIndicatorView;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.utils.PatternHelper;
import com.nyq.projecttreasure.utils.SPUtils;
import com.nyq.projecttreasure.views.RippleLockerHitCellView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WholePatternSettingActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.menu)
    TextView menu;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.text_msg)
    TextView textMsg;
    @BindView(R.id.pattern_indicator_view)
    PatternIndicatorView patternIndicatorView;
    @BindView(R.id.pattern_lock_view)
    PatternLockerView patternLockView;
    @BindView(R.id.cz)
    TextView cz;
    @BindView(R.id.qd)
    TextView qd;
    private PatternHelper patternHelper;
    private boolean isFrist = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_pattern_setting);
        ButterKnife.bind(this);
        title.setText("设置手势密码");
        setStatusBarColor(R.color.colorPrimary);
        toolbar.setBackgroundResource(R.color.colorPrimary);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final IHitCellView hitCellView = new RippleLockerHitCellView()
                .setHitColor(patternLockView.getHitColor())
                .setErrorColor(patternLockView.getErrorColor());

        patternLockView.setHitCellView(hitCellView)
                .build();

        patternLockView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                boolean isOk = isPatternOk(hitList);
                view.updateStatus(!isOk);
                patternIndicatorView.updateState(hitList, !isOk);
                updateMsg();
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

        textMsg.setText("设置解锁图案");
        patternHelper = new PatternHelper();

        //确定
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrist = false;
                SPUtils.put(WholePatternSettingActivity.this,"isChecked",true);
                patternHelper.validateForSetting(hitLists,isFrist);
                patternLockView.clearHitState();
            }
        });
    }

    List<Integer> hitLists;
    private boolean isPatternOk(List<Integer> hitList) {
        hitLists = new ArrayList<>();
        hitLists.addAll(hitList);
        patternHelper.validateForSetting(hitList,isFrist);
        return patternHelper.isOk();
    }

    private void updateMsg() {
        textMsg.setText(patternHelper.getMessage());
        textMsg.setTextColor(patternHelper.isOk() ?
                getResources().getColor(R.color.midnightblue) :
                getResources().getColor(R.color.orangered));
        if (patternHelper.getMessage().equals(patternHelper.getSettingSuccessMsg())) {
            qd.setVisibility(View.VISIBLE);
        } else {
            qd.setVisibility(View.GONE);
            Timer timer = new Timer();// 实例化Timer类
            timer.schedule(new TimerTask() {
                public void run() {
                    patternLockView.clearHitState();
                    this.cancel();
                }
            }, 600);// 这里百毫秒
        }
    }

    private void finishIfNeeded() {
        if (patternHelper.isFinish()) {
            finish();
        }
    }
}
