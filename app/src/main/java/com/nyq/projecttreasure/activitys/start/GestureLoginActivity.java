package com.nyq.projecttreasure.activitys.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.ihsg.patternlocker.IHitCellView;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.main.MainActivity;
import com.nyq.projecttreasure.activitys.setting.WholePatternCheckingActivity;
import com.nyq.projecttreasure.activitys.setting.WholePatternSettingActivity;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.PatternHelper;
import com.nyq.projecttreasure.views.RippleLockerHitCellView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GestureLoginActivity extends BaseActivity {

    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.text_msg)
    TextView textMsg;
    @BindView(R.id.pattern_lock_view)
    PatternLockerView patternLockView;
    @BindView(R.id.tv_wjss)
    TextView tvWjss;
    private PatternHelper patternHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**全屏设置，隐藏窗口所有装饰**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gesture_login);
        ButterKnife.bind(this);
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
                updateMsg();
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

        textMsg.setText("");
        patternHelper = new PatternHelper();
    }

    private boolean isPatternOk(List<Integer> hitList) {
        patternHelper.validateForChecking(hitList);
        return patternHelper.isOk();
    }

    private void updateMsg() {
        textMsg.setText(patternHelper.getMessage());
        textMsg.setTextColor(patternHelper.isOk() ?
                getResources().getColor(R.color.colorPrimaryDark) :
                getResources().getColor(R.color.orangered));
        if (patternHelper.isOk()) {
            Timer timer = new Timer();// 实例化Timer类
            timer.schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(GestureLoginActivity.this,MainActivity.class));
                    patternLockView.clearHitState();

                    AGCache.USER_ACCOUNT = "niuyq";
                    AGCache.USER_PSW = "123456789";
                    this.cancel();
                }
            }, 600);// 这里百毫秒
        } else {
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
