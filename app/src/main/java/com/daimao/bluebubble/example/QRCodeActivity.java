package com.daimao.bluebubble.example;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.util.QRCodeTools;
import com.daimao.bluebubble.util.Tools;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public class QRCodeActivity extends XActivity implements View.OnClickListener {

    @BindView(R.id.edit_input)
    EditText mEditInput;

    @BindView(R.id.img_qrcode)
    ImageView mImgQRCode;

    @BindView(R.id.btn_create_qrcode)
    Button mBtnCreateQRCode;

    @BindView(R.id.btn_recognise_qrcode)
    Button mBtnRecogniseQRCode;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBtnCreateQRCode.setOnClickListener(this);
        mBtnRecogniseQRCode.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_qrcode:
                String text = mEditInput.getText().toString();
                if(Tools.isEmpty(text)){
                    BaseApplication.getInstance().showTip("请输入文本");
                }else{
                    Bitmap bitmap = null;
                    try {
                        bitmap = QRCodeTools.encodeAsBitmap(new String(text.getBytes(), "ISO-8859-1"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        mImgQRCode.setImageBitmap(bitmap);
                    }else{
                        BaseApplication.getInstance().showTip("二维码生成失败");
                    }
                }
                break;
            case R.id.btn_recognise_qrcode:
                BaseApplication.getInstance().showTip("识别二维码");
                break;
            default:
                break;
        }
    }
}
