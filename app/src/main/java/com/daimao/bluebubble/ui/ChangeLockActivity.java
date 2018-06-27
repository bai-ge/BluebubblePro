package com.daimao.bluebubble.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.daimao.bluebubble.AppConfigure;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.util.Tools;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.cache.SharedPref;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public class ChangeLockActivity extends XActivity {
    @BindView(R.id.txt_inform)
    TextView mTxtInfrom;

    @BindView(R.id.pattern_lock_view)
    PatternLockerView mPatternLockerView;

    private String mGesturePassword;

    private SharedPref mSharedPref;

    private boolean isVerified;

    @Override
    public void initData(Bundle savedInstanceState) {
        mSharedPref = SharedPref.getInstance(getApplicationContext());
        mGesturePassword = mSharedPref.getString(AppConfigure.KEY_GESTURE_PASSWORD, "");

        showInform("请输入密码");
        mPatternLockerView.setOnPatternChangedListener(mCheckPatternChangeListener);
    }


    private OnPatternChangeListener mCheckPatternChangeListener = new OnPatternChangeListener() {
        @Override
        public void onStart(PatternLockerView patternLockerView) {
            showInform("");
        }

        @Override
        public void onChange(PatternLockerView patternLockerView, List<Integer> list) {

        }

        @Override
        public void onComplete(PatternLockerView patternLockerView, List<Integer> list) {
            if (list != null && list.size() >= 4) {
                String pas = list.toString();
                if (mGesturePassword.equals(pas)) {
                    mSharedPref.putString(AppConfigure.KEY_GESTURE_PASSWORD, pas);
//                    Router.newIntent(ChangeLockActivity.this).to(MainActivity.class).launch();
                    isVerified = true;
                    mPatternLockerView.setOnPatternChangedListener(mChangePatternChangeListener);
                    mGesturePassword = null;
                    showInform("请绘制新密码");
                } else {
                    showInform("手势密码错误，请重新绘制");
                }
            } else {
                showInform("手势密码错误，请重新绘制");
            }
        }

        @Override
        public void onClear(PatternLockerView patternLockerView) {

        }
    };

    private OnPatternChangeListener mChangePatternChangeListener = new OnPatternChangeListener() {
        @Override
        public void onStart(PatternLockerView patternLockerView) {

        }

        @Override
        public void onChange(PatternLockerView patternLockerView, List<Integer> list) {

        }

        @Override
        public void onComplete(PatternLockerView patternLockerView, List<Integer> list) {
            if (Tools.isEmpty(mGesturePassword)) {
                if (list != null && list.size() >= 4) {
                    mGesturePassword = list.toString();
                    showInform("请再次绘制解锁图案");
                } else {
                    showInform("至少连接四个点，请重新绘制");
                }
            } else {
                if (list != null && list.size() >= 4) {
                    String pas = list.toString();
                    if (mGesturePassword.equals(pas)) {
                        mSharedPref.putString(AppConfigure.KEY_GESTURE_PASSWORD, pas);
//                        Router.newIntent(ChangeLockActivity.this).to(MainActivity.class).launch();
                        onBackPressed();
                    } else {
                        showInform("与上次绘制不一致，请重新绘制");
                        mGesturePassword = "";
                    }
                } else {
                    showInform("至少连接四个点，请重新绘制");
                    mGesturePassword = "";
                }
            }
        }

        @Override
        public void onClear(PatternLockerView patternLockerView) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.act_lock;
    }

    @Override
    public Object newP() {
        return null;
    }

    private void showInform(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtInfrom.setText(text);
            }
        });
    }
}
