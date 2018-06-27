package com.daimao.bluebubble.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimao.bluebubble.AppConfigure;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.example.TestActivity;
import com.daimao.bluebubble.util.Tools;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.cache.SharedPref;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;


/**
 * Created by baige on 2018/6/17.
 */

public class LockActivity extends XActivity {

    @BindView(R.id.txt_inform)
    TextView mTxtInfrom;

    @BindView(R.id.pattern_lock_view)
    PatternLockerView mPatternLockerView;

    @BindView(R.id.btn_test)
    Button mBtnTest;

    private String mGesturePassword;

    private SharedPref mSharedPref;



    @Override
    public void initData(Bundle savedInstanceState) {
        mSharedPref = SharedPref.getInstance(getApplicationContext());
        mGesturePassword = mSharedPref.getString(AppConfigure.KEY_GESTURE_PASSWORD, "");
        if (Tools.isEmpty(mGesturePassword)) {
            mTxtInfrom.setText("设置手势密码");
            mPatternLockerView.setOnPatternChangedListener(mNewPatternChangeListener);
        } else {
            mTxtInfrom.setText("");
            mPatternLockerView.setOnPatternChangedListener(mCheckPatternChangeListener);
        }
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.newIntent(LockActivity.this).to(TestActivity.class).launch();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_lock;
    }

    @Override
    public Object newP() {
        return null;
    }

    private OnPatternChangeListener mNewPatternChangeListener = new OnPatternChangeListener() {
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
                        Router.newIntent(LockActivity.this).to(MainActivity.class).launch();
                    }else{
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
                    Router.newIntent(LockActivity.this).to(MainActivity.class).launch();
                }else{
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

    private void showInform(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtInfrom.setText(text);
            }
        });
    }
}
