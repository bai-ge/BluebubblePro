package com.daimao.bluebubble.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by Ant on 2018/6/24.
 */

public class AddPwdActivity extends XActivity {

    private static final String[] groupNameStr = {"社交", "工作", "学习", "生活", "娱乐", "其它"};
    private ArrayAdapter<String> adapter;

    private AlertDialog groupDialog;

    //    @BindView(R.id.spin_group)
    Spinner spinner;

    @BindView(R.id.et_group)
    EditText groupEditText;

    @BindView(R.id.btn_select_group)
    TextView groupBtn;    // 分组的编辑按钮

    @BindView(R.id.btn_lock)
    ImageButton lockImgBtn;

    @BindView(R.id.btn_share)
    ImageButton shareImgBtn;

    private boolean isLock;

    @Override
    public void initData(Bundle savedInstanceState) {
        /* ----使用 spinner 下拉框---- */
        // 将可选内容与ArrayAdapter连接起来
        // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupNameStr);
        // 设置下拉列表的风格
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
        // spinner.setAdapter(adapter);
        // 添加事件Spinner事件监听
        // spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        // 分组 按钮 -> 选择已有项
        groupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGroupDialog();
            }
        });


        // 上锁、解锁
        lockImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLock = !isLock;
                if (isLock) {
                    lockImgBtn.setBackgroundResource(R.drawable.ic_lock);
                    BaseApplication.getInstance().showTip("上锁");
                } else {
                    lockImgBtn.setBackgroundResource(R.drawable.ic_unlock);
                    BaseApplication.getInstance().showTip("解锁");
                }
            }
        });

        // 分享
        shareImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.getInstance().showTip("分享");
            }
        });

    }

    // Spinner 使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            BaseApplication.getInstance().showTip(groupNameStr[i]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    /**
     * 选择分组对话框
     */
    public void selectGroupDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddPwdActivity.this);
        builder.setTitle("分组");
        builder.setCancelable(true); //设置是否可以点击对话框外部取消
        builder.setSingleChoiceItems(groupNameStr, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                // 所有选项索引都大于0，按钮索引都小于0
                if (item >= 0) {
                    BaseApplication.getInstance().showTip(groupNameStr[item]);
                    // 关闭
                    groupDialog.dismiss();
                    groupEditText.setText(groupNameStr[item]);
                } else if (item == DialogInterface.BUTTON_POSITIVE) {
                    // 确定按钮
                    BaseApplication.getInstance().showTip("已选择：" + groupNameStr[item]);
                    // 关闭
                    groupDialog.dismiss();
                    groupEditText.setText(groupNameStr[item]);
                } else if (item == DialogInterface.BUTTON_NEGATIVE) {
                    // 取消按钮
                    BaseApplication.getInstance().showTip("没有选择");
                    // 关闭
                    groupDialog.dismiss();
                    groupEditText.setText("请输入分组");
                }
            }
        });
        groupDialog = builder.show();
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param view
     */
    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_passwordbook_detail;
    }

    @Override
    public Object newP() {
        return null;
    }
}
