package com.nyq.projecttreasure.activitys.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
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
import com.nyq.projecttreasure.views.RippleLockerHitCellView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WholePatternCheckingActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_pattern_setting);
        ButterKnife.bind(this);
        title.setText("确认手势密码");
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
                boolean isError = !isPatternOk(hitList);
                view.updateStatus(isError);
                patternIndicatorView.updateState(hitList, isError);
                updateMsg();
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

        textMsg.setText("确认之前所设的图案");
        patternHelper = new PatternHelper();
    }

    private boolean isPatternOk(List<Integer> hitList) {
        patternHelper.validateForChecking(hitList);
        return patternHelper.isOk();
    }

    private void updateMsg() {
        textMsg.setTextColor(patternHelper.isOk() ?
                getResources().getColor(R.color.colorPrimaryDark) :
                getResources().getColor(R.color.orangered));
        if (patternHelper.isOk()) {
            Timer timer = new Timer();// 实例化Timer类
            timer.schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(WholePatternCheckingActivity.this,WholePatternSettingActivity.class));
                    patternLockView.clearHitState();
                    this.cancel();
                }
            }, 600);// 这里百毫秒
        } else {
            textMsg.setText(patternHelper.getMessage());
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
