package com.daimao.bluebubble.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.util.BitmapTools;
import com.daimao.bluebubble.util.QRCodeTools;
import com.daimao.bluebubble.util.Tools;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;

public class QRCodeActivity extends XActivity implements View.OnClickListener {

    @BindView(R.id.edit_input)
    EditText mEditInput;

    @BindView(R.id.img_qrcode)
    ImageView mImgQRCode;

    @BindView(R.id.btn_create_qrcode)
    Button mBtnCreateQRCode;

    @BindView(R.id.btn_create_qrcode_with_logo)
    Button mBtnCreateQRCodeWithLogo;


    @BindView(R.id.btn_recognise_qrcode)
    Button mBtnRecogniseQRCode;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBtnCreateQRCode.setOnClickListener(this);
        mBtnCreateQRCodeWithLogo.setOnClickListener(this);
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
        String text = null;
        switch (v.getId()) {
            case R.id.btn_create_qrcode:
                 text = mEditInput.getText().toString();
                if (Tools.isEmpty(text)) {
                    BaseApplication.getInstance().showTip("请输入文本");
                } else {
                    Bitmap bitmap = null;
                    try {
                        bitmap = QRCodeTools.encodeAsBitmap(new String(text.getBytes(), "ISO-8859-1"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        mImgQRCode.setImageBitmap(bitmap);
                    } else {
                        BaseApplication.getInstance().showTip("二维码生成失败");
                    }
                }
                break;
            case R.id.btn_create_qrcode_with_logo:
                text = mEditInput.getText().toString();
                if (Tools.isEmpty(text)) {
                    BaseApplication.getInstance().showTip("请输入文本");
                } else {
                    Bitmap bitmap = null;
                    try {
                        Bitmap logo = BitmapTools.decodeSampledBitmapFromResource(getResources(), R.drawable.lock, 200, 200);
                        Log.d(LOG_TAG, logo+"图片");
                        logo = BitmapTools.drawCircleViewWithBorder(logo, 200, 200, 15, Color.WHITE);
                        logo = BitmapTools.drawCircleView(logo, 200, 200);
//                        bitmap = QRCodeTools.encodeAsBitmap(new String(text.getBytes(), "ISO-8859-1"), logo);
                        bitmap = QRCodeTools.encodeAsBitmap(new String(text.getBytes(), "ISO-8859-1"), logo);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        mImgQRCode.setImageBitmap(bitmap);
                    } else {
                        BaseApplication.getInstance().showTip("二维码生成失败");
                    }
                }
                break;
            case R.id.btn_recognise_qrcode:
                BaseApplication.getInstance().showTip("识别二维码");
                customScan();
                break;
            default:
                break;
        }
    }

    // 你也可以使用简单的扫描功能，但是一般扫描的样式和行为都是可以自定义的，这里就写关于自定义的代码了
// 你可以把这个方法作为一个点击事件
    public void customScan() {
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("扫描条形码/二维码")
                .setTimeout(30000)
                .initiateScan(); // 初始化扫描

//        IntentIntegrator integrator = new IntentIntegrator((Activity) activity);
//        integrator.setCaptureActivity(CodeScanningActivity.class);
//        integrator.setOrientationLocked(true);
//        integrator.setBeepEnabled(false);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
    }

    @Override
// 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String scanResult = intentResult.getContents();
                mEditInput.setText(scanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
