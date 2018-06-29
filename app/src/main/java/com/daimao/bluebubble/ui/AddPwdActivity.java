package com.daimao.bluebubble.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.view.CircleImageView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by Ant on 2018/6/24.
 */

public class AddPwdActivity extends XActivity implements View.OnTouchListener, View.OnClickListener{

    private static final String[] groupNameStr = {"社交", "工作", "学习", "生活", "娱乐", "其它"};
    private ArrayAdapter<String> adapter;

    private AlertDialog groupDialog;

    //    @BindView(R.id.spin_group)
    Spinner spinner;

    @BindView(R.id.btn_select_group)
    TextView mBtnGroup;    // 分组的编辑按钮


    /***************************/
    @BindView(R.id.btn_lock)
    ImageButton mBtnLock;

    @BindView(R.id.btn_share_qrcode)
    ImageButton mBtnShareQrcode;

    @BindView(R.id.btn_share)
    ImageButton mBtnShare;

    @BindView(R.id.btn_help)
    ImageButton mBtnHelp;

    @BindView(R.id.img_logo)
    CircleImageView mImgLogo;

    @BindView(R.id.ll_title)
    ViewGroup mLayoutTitle;

    @BindView(R.id.et_title)
    EditText mEtTitle;

    @BindView(R.id.ll_account)
    ViewGroup mLayoutAccount;

    @BindView(R.id.et_account)
    EditText mEtAccount;

    @BindView(R.id.ll_password)
    ViewGroup mLayoutPassword;

    @BindView(R.id.et_password)
    EditText mEtPassword;

    @BindView(R.id.checkbox_password)
    CheckBox mCheckPassword;

    @BindView(R.id.ll_address)
    ViewGroup mLayoutAddress;

    @BindView(R.id.et_address)
    EditText mEtAddress;

    @BindView(R.id.ll_group)
    ViewGroup mLayoutGroup;

    @BindView(R.id.et_group)
    EditText mEtGroup;

    @BindView(R.id.txt_group)
    TextView mTxtGroup;

    @BindView(R.id.ll_remark)
    ViewGroup mLayoutRemark;

    @BindView(R.id.et_remark)
    EditText mEtRemark;

    @BindView(R.id.btn_delete)
    Button mBtnDelete;

    @BindView(R.id.btn_save)
    Button mBtnSave;


    private boolean mIsLock;

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

        mLayoutTitle.setOnClickListener(this);
        mLayoutAccount.setOnClickListener(this);
        mLayoutPassword.setOnClickListener(this);
        mLayoutAddress.setOnClickListener(this);
        mLayoutGroup.setOnClickListener(this);
        mLayoutRemark.setOnClickListener(this);
        // 分组 按钮 -> 选择已有项
        mBtnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGroupDialog();
            }
        });

        mEtRemark.setOnTouchListener(this);


        // 上锁、解锁
        mBtnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsLock = !mIsLock;
                if (mIsLock) {
                    mBtnLock.setBackgroundResource(R.drawable.ic_lock);
                    BaseApplication.getInstance().showTip("上锁");
                } else {
                    mBtnLock.setBackgroundResource(R.drawable.ic_unlock);
                    BaseApplication.getInstance().showTip("解锁");
                }
            }
        });

        // 分享
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.getInstance().showTip("分享");
            }
        });

        mCheckPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if(isCheck){
                    //选择状态 显示明文--设置为可见的密码
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEtPassword.setSelection(mEtPassword.getText().length());
                }else{
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtPassword.setSelection(mEtPassword.getText().length());
                }
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
                    mEtGroup.setText(groupNameStr[item]);
                    mEtGroup.setSelection(mEtGroup.getText().length());
                } else if (item == DialogInterface.BUTTON_POSITIVE) {
                    // 确定按钮
                    BaseApplication.getInstance().showTip("已选择：" + groupNameStr[item]);
                    // 关闭
                    groupDialog.dismiss();
                    mEtGroup.setText(groupNameStr[item]);
                    mEtGroup.setSelection(mEtGroup.getText().length());
                } else if (item == DialogInterface.BUTTON_NEGATIVE) {
                    // 取消按钮
                    BaseApplication.getInstance().showTip("没有选择");
                    // 关闭
                    groupDialog.dismiss();
                    mEtGroup.setText("请输入分组");
                    mEtGroup.setSelection(mEtGroup.getText().length());
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


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.et_remark && canVerticalScroll(mEtRemark))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_title:
                mEtTitle.requestFocus();
                mEtTitle.setSelection(mEtTitle.getText().length());
                break;
            case R.id.ll_account:
                mEtAccount.requestFocus();
                mEtAccount.setSelection(mEtAccount.getText().length());
                break;
            case R.id.ll_password:
                mEtPassword.requestFocus();
                mEtPassword.setSelection(mEtPassword.getText().length());
                break;
            case R.id.ll_address:
                mEtAddress.requestFocus();
                mEtAddress.setSelection(mEtAddress.getText().length());
                break;
            case R.id.ll_group:
                mEtGroup.requestFocus();
                mEtGroup.setSelection(mEtGroup.getText().length());
                break;
            case R.id.ll_remark:
                mEtRemark.requestFocus();
                mEtRemark.setSelection(mEtRemark.getText().length());
                break;
        }
    }
}
