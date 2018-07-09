package com.daimao.bluebubble.example;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimao.bluebubble.R;
import com.daimao.bluebubble.util.BitmapTools;
import com.daimao.bluebubble.util.ColorSelectDialog;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;

public class LogoActivity extends XActivity implements View.OnClickListener{

    @BindView(R.id.et_title)
    EditText mEditTitle;

    @BindView(R.id.btn_refresh)
    Button mBtnRefresh;

    @BindView(R.id.img_logo)
    ImageView mImgLogo;

    @BindView(R.id.btn_graph)
    Button mBtnGraph;

    @BindView(R.id.btn_background)
    Button mBtnBackground;

    @BindView(R.id.btn_front_color)
    Button mBtnFrontColor;

    private ColorSelectDialog backgroundColorSelectDialog;

    private ColorSelectDialog fontColorSelectDialog;

    private int background;

    private int frontColor;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBtnRefresh.setOnClickListener(this);
        mBtnGraph.setOnClickListener(this);
        mBtnBackground.setOnClickListener(this);
        mBtnFrontColor.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_logo;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
                String text = mEditTitle.getText().toString();
                Log.d(LOG_TAG, text.substring(0, 1));
                Bitmap bitmap = BitmapTools.createCircleLogo(mEditTitle.getText().toString().substring(0, 1), 120, background, frontColor);
                mImgLogo.setImageBitmap(bitmap);
                break;
            case R.id.btn_graph:

                break;
            case R.id.btn_background:
                if (backgroundColorSelectDialog == null) {
                    backgroundColorSelectDialog = new ColorSelectDialog(this);
                    backgroundColorSelectDialog.setOnColorSelectListener(new ColorSelectDialog.OnColorSelectListener() {
                        @Override
                        public void onSelectFinish(int color) {
                            background = color;

                        }
                    });
                }
                backgroundColorSelectDialog.setLastColor(background);
                backgroundColorSelectDialog.show();
                break;
            case R.id.btn_front_color:
                if (fontColorSelectDialog == null) {
                    fontColorSelectDialog = new ColorSelectDialog(this);
                    fontColorSelectDialog.setOnColorSelectListener(new ColorSelectDialog.OnColorSelectListener() {
                        @Override
                        public void onSelectFinish(int color) {
                            frontColor = color;

                        }
                    });
                }
                fontColorSelectDialog.setLastColor(frontColor);
                fontColorSelectDialog.show();
                break;
        }
    }
}
