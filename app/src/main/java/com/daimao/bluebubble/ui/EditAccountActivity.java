package com.daimao.bluebubble.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.data.dao.AccountDAO;
import com.daimao.bluebubble.data.dao.impl.AccountDAOImpl;
import com.daimao.bluebubble.data.model.AccountEntity;
import com.daimao.bluebubble.exception.SqlException;
import com.daimao.bluebubble.listener.DeleteListener;
import com.daimao.bluebubble.listener.SaveListener;
import com.daimao.bluebubble.listener.UpdateListener;
import com.daimao.bluebubble.util.BitmapTools;
import com.daimao.bluebubble.util.FileUtils;
import com.daimao.bluebubble.util.GlideImageLoader;
import com.daimao.bluebubble.util.ImageLoader;
import com.daimao.bluebubble.util.QRCodeTools;
import com.daimao.bluebubble.util.Tools;
import com.daimao.bluebubble.view.CircleImageView;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Codec;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import cn.droidlover.xdroidmvp.mvp.XActivity;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;

/**
 * Created by Ant on 2018/6/24.
 */

public class EditAccountActivity extends XActivity implements View.OnTouchListener, View.OnClickListener, TextWatcher {

    public final static String Charset = "ISO-8859-1";
    private static final String[] groupNameStr = {"社交", "工作", "学习", "生活", "娱乐", "其它"};
    private ArrayAdapter<String> adapter;

    private AlertDialog groupDialog;

    //    @BindView(R.id.spin_group)
    Spinner spinner;

    @BindView(R.id.btn_select_group)
    TextView mBtnGroup;    // 分组的编辑按钮


    /***************************/
    @BindView(R.id.checkbox_lock)
    CheckBox mCheckboxLock;

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


    private AccountEntity mAccountEntity;

    private boolean mIsChange; //数据是否被更改

    private boolean mIsAddAccount; //是否添加账号

    private boolean[] isCheckArray = new boolean[]{true, true, true, true, true, true};

    private ImagePicker mImagePicker;

    private final int IMAGE_PICKER = 100;

    private ArrayList<ImageItem> images = null;

    /**
     * @param savedInstanceState
     */
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("account")) {
                mAccountEntity = bundle.getParcelable("account");
            } else {
                mAccountEntity = new AccountEntity();
                mAccountEntity.setGroupName(groupNameStr[0]);
            }
            if (bundle.containsKey("add")) {
                setIsAddAccount(bundle.getInt("add") != 0);
                if (bundle.containsKey("account")) {
                    setIsChange(true);
                }
            }
        }
        mImagePicker = ImagePicker.getInstance();
        mImagePicker.setImageLoader(new GlideImageLoader());//设置图片加载器

        mImagePicker.setMultiMode(false);
        mImagePicker.setStyle(CropImageView.Style.CIRCLE);


        mImagePicker.setShowCamera(true);  //显示拍照按钮
        mImagePicker.setCrop(true);        //允许裁剪（单选才有效）
        mImagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        mImagePicker.setSelectLimit(9);    //选中数量限制
//        mImagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        mImagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        mImagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        mImagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        mImagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {

        mCheckboxLock.setChecked(mAccountEntity.isLock());

        //TODO 设置Logo
        if (!Tools.isEmpty(mAccountEntity.getLogoPath())) {
            Bitmap bitmap = ImageLoader.getInstance().getBitmapFromMemoryCache(mAccountEntity.getLogoPath());
            if (bitmap == null) {
                bitmap = ImageLoader.decodeSampledBitmapFromResource(mAccountEntity.getLogoPath(), 120);
                if (bitmap != null) {
                    ImageLoader.getInstance().addBitmapToMemoryCache(mAccountEntity.getLogoPath(), bitmap);
                }
            }
            if (bitmap != null) {
                mImgLogo.setImageBitmap(bitmap);
            }
        }


        mEtTitle.setText(mAccountEntity.getTitle());
        mEtAccount.setText(mAccountEntity.getAccount());
        mEtPassword.setText(mAccountEntity.getPassword());
        mEtAddress.setText(mAccountEntity.getUrl());
        mEtGroup.setText(mAccountEntity.getGroupName());
        mEtRemark.setText(mAccountEntity.getRemark());

        setOnTextChange();

        mLayoutTitle.setOnClickListener(this);
        mLayoutAccount.setOnClickListener(this);
        mLayoutPassword.setOnClickListener(this);
        mLayoutAddress.setOnClickListener(this);
        mLayoutGroup.setOnClickListener(this);
        mLayoutRemark.setOnClickListener(this);


        mEtRemark.setOnTouchListener(this);

        // 分组 按钮 -> 选择已有项
        mBtnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGroupDialog();
            }
        });


        mBtnShareQrcode.setOnClickListener(this);

        // 分享
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.getInstance().showTip("分享");
                desTest();
            }
        });

        mBtnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });

        mCheckPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck) {
                    //选择状态 显示明文--设置为可见的密码
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEtPassword.setSelection(mEtPassword.getText().length());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtPassword.setSelection(mEtPassword.getText().length());
                }
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSaveOrUpdate();
            }

        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAskDelete();
            }
        });

        mImgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagePicker.setMultiMode(false);
                mImagePicker.setStyle(CropImageView.Style.CIRCLE);

                int size = mImgLogo.getWidth();
                Integer radius = size / 3;
                radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());
                Log.d(LOG_TAG, "radius =" + radius);
                mImagePicker.setFocusWidth(radius * 2);
                mImagePicker.setFocusHeight(radius * 2);
                mImagePicker.setOutPutX(radius * 2);
                mImagePicker.setOutPutY(radius * 2);

                Intent intent = new Intent(EditAccountActivity.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onResult()" + requestCode);
        if (data != null && requestCode == IMAGE_PICKER) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            int size = mImgLogo.getWidth();
            File headPath = new File(BaseApplication.logoImgPath);
            if(!headPath.exists()){
                headPath.mkdirs();
            }
            File oldFile = new File(images.get(0).path);
            File imgFile = new File(headPath, oldFile.getName());
            FileUtils.moveTo(oldFile, imgFile);
            images.get(0).path = imgFile.getAbsolutePath();
            Log.d(LOG_TAG, "img  =" + images.get(0).toString() + "name:"+images.get(0).name);
            Log.d(LOG_TAG, "截图图片宽度："+size);
            Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(images.get(0).path, size);
            if (bitmap != null) {
                ImageLoader.getInstance().addBitmapToMemoryCache(images.get(0).path, bitmap);
               mImgLogo.setImageBitmap(bitmap);
            }
            mAccountEntity.setLogoPath(images.get(0).path);
            setIsChange(true);
            // mImagePicker.getImageLoader().displayImage(getActivity(), images.get(0).path, mCircleImageView, size / 4, size / 4);
            Log.d(LOG_TAG, "img path =" + images.get(0).path);
        } else {
            BaseApplication.getInstance().showTip("没有数据");
            Log.d(LOG_TAG, "onResult()" + null);
        }
    }

    private void doSaveOrUpdate() {
        if (mIsAddAccount) {
            //TODO 保存账号
//                    getContentResolver().insert(AccountDAO.TAB_URI, )
            AccountDAOImpl accountDAO = new AccountDAOImpl();
            mAccountEntity.setCreatTime(System.currentTimeMillis());
            accountDAO.doSave(mAccountEntity, new SaveListener<AccountEntity>() {
                @Override
                public void done(AccountEntity entity, SqlException e) {
                    BaseApplication.getInstance().showTip("保存" + entity.getId());
                    if (e.getErrorCode() == SqlException.SUCCESS) {
                        setIsChange(false);
                        setIsAddAccount(false);
                    }
                }
            });
        } else {
            //TODO 更新账号
            AccountDAOImpl accountDAO = new AccountDAOImpl();
            accountDAO.doUpdate(mAccountEntity, new UpdateListener() {
                @Override
                public void done(SqlException e) {
                    if (e.getErrorCode() == SqlException.SUCCESS) {
                        setIsChange(false);
                    }
                }
            });
        }
    }


    private void setIsChange(boolean isChange) {
        this.mIsChange = isChange;
        mBtnSave.setEnabled(isChange);
    }

    private void setIsAddAccount(boolean isAddAccount) {
        this.mIsAddAccount = isAddAccount;
        mBtnDelete.setEnabled(!isAddAccount);
    }

    private void setOnTextChange() {
        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAccountEntity.setTitle(mEtTitle.getText().toString());
                if (!mIsChange) {
                    mIsChange = true;
                    mBtnSave.setEnabled(true);
                }
            }
        });

        mEtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAccountEntity.setAccount(mEtAccount.getText().toString());
                if (!mIsChange) {
                    mIsChange = true;
                    mBtnSave.setEnabled(true);
                }
            }
        });

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAccountEntity.setPassword(mEtPassword.getText().toString());
                if (!mIsChange) {
                    mIsChange = true;
                    mBtnSave.setEnabled(true);
                }
            }
        });

        mEtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAccountEntity.setUrl(mEtAddress.getText().toString());
                if (!mIsChange) {
                    mIsChange = true;
                    mBtnSave.setEnabled(true);
                }
            }
        });

        mEtGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAccountEntity.setGroupName(mEtGroup.getText().toString());
                if (!mIsChange) {
                    mIsChange = true;
                    mBtnSave.setEnabled(true);
                }
            }
        });
        mEtRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAccountEntity.setRemark(mEtRemark.getText().toString());
                if (!mIsChange) {
                    mIsChange = true;
                    mBtnSave.setEnabled(true);
                }
            }
        });

    }

    private void showAskDelete() {
        new AlertDialogWrapper.Builder(this)
                .setTitle("提示")
                .setMessage("是否删除该账号信息")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 删除账号
                        AccountDAOImpl accountDAO = new AccountDAOImpl();
                        accountDAO.doDelete(mAccountEntity.getId(), new DeleteListener() {
                            @Override
                            public void done(SqlException e) {
                                if (e.getErrorCode() == SqlException.SUCCESS) {
                                    EditAccountActivity.super.onBackPressed();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!mIsChange) {
            mIsChange = true;
            mBtnSave.setEnabled(true);
        }

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


    private void showHelpDialog() {

        new AlertDialogWrapper.Builder(this)
                .setTitle("帮助")
                .setMessage(getApplicationContext().getString(R.string.help_tips_detail))
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
                .show();

    }

    private Dialog dialog;

    public void showQRCode() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View root = LayoutInflater.from(this).inflate(R.layout.dialog_qrcode, null);

        DialogViewHolder dialogViewHolder = new DialogViewHolder(root);
        initDialogViewHolder(dialogViewHolder);
        //初始化控件
//        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
//        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
//        choosePhoto.setOnClickListener(this);
//        takePhoto.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(root);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }


    private void initDialogViewHolder(final DialogViewHolder dialogViewHolder) {
        refreshQRCodeImg(dialogViewHolder);
        dialogViewHolder.titleCheckBox.setChecked(isCheckArray[0]);
        dialogViewHolder.accountCheckBox.setChecked(isCheckArray[1]);
        dialogViewHolder.passwordCheckBox.setChecked(isCheckArray[2]);
        dialogViewHolder.urlCheckBox.setChecked(isCheckArray[3]);
        dialogViewHolder.groupCheckBox.setChecked(isCheckArray[4]);
        dialogViewHolder.remarkCheckBox.setChecked(isCheckArray[5]);

        dialogViewHolder.titleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckArray[0] = b;
                refreshQRCodeImg(dialogViewHolder);
            }
        });

        dialogViewHolder.accountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckArray[1] = b;
                refreshQRCodeImg(dialogViewHolder);
            }
        });

        dialogViewHolder.passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckArray[2] = b;
                refreshQRCodeImg(dialogViewHolder);
            }
        });

        dialogViewHolder.urlCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckArray[3] = b;
                refreshQRCodeImg(dialogViewHolder);
            }
        });

        dialogViewHolder.groupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckArray[4] = b;
                refreshQRCodeImg(dialogViewHolder);
            }
        });

        dialogViewHolder.remarkCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckArray[5] = b;
                refreshQRCodeImg(dialogViewHolder);
            }
        });
    }

    private void refreshQRCodeImg(DialogViewHolder dialogViewHolder) {
        Bitmap logo = null;
        if(!Tools.isEmpty(mAccountEntity.getLogoPath())){
            logo = ImageLoader.getInstance().getBitmapFromMemoryCache(mAccountEntity.getLogoPath());
            logo = BitmapTools.drawCircleViewWithBorder(logo, logo.getWidth(), logo.getHeight(), 8, Color.WHITE);
        }

        Bitmap bitmap = QRCodeTools.encodeAsBitmap(getAccountEntityMessage(), logo);
        if (bitmap != null) {
            dialogViewHolder.imgView.setImageBitmap(bitmap);
        } else {
            dialogViewHolder.imgView.setBackgroundResource(R.drawable.ic_qrcode);
        }
    }


    /**
     * 选择分组对话框
     */
    public void selectGroupDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
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


    public void desTest() {
        Gson gson = new Gson();
        AccountEntity.Account account = new AccountEntity.Account();
        account.setT("标题");
        account.setA("账号");
        account.setP("密码");
        account.setU("网址");
        account.setG("分组");
        account.setR("备注");
        String text = gson.toJson(account);
        try {
            String key = Codec.DES.initKey();
            System.out.println("key = " + key);

            System.out.println("加密前：" + text + ", len =" + text.getBytes().length);

            byte[] buf = Codec.DES.encrypt(text.getBytes(), key);

            String data = Codec.BASE64.encodeToString(buf);

            System.out.println("加密后：buf len =" + buf.length);
            System.out.println("加密后：data :" + data + ", len =" + data.length());

            buf = Codec.DES.decrypt(buf, key);

            String deStr = new String(buf, "utf-8");
            System.out.println("解密后：" + deStr);


        } catch (Exception e) {
            e.printStackTrace();
        }
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
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_qrcode:
                getAccountEntityMessage();
                showQRCode();
                break;
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

    public String getAccountEntityMessage() {
        AccountEntity.Account account = new AccountEntity.Account();
        if (isCheckArray[0]) {
            account.setT(changeCharset(mEtTitle.getText().toString()));
        }

        if (isCheckArray[1]) {
            account.setA(changeCharset(mEtAccount.getText().toString()));
        }

        if (isCheckArray[2]) {
            account.setP(changeCharset(mEtPassword.getText().toString()));
        }
        if (isCheckArray[3]) {
            account.setU(changeCharset(mEtAddress.getText().toString()));
        }
        if (isCheckArray[4]) {
            account.setG(changeCharset(mEtGroup.getText().toString()));
        }
        if (isCheckArray[5]) {
            account.setR(changeCharset(mEtRemark.getText().toString()));
        }

        Gson gson = new Gson();
        try {
            String output = new String(gson.toJson(account).getBytes(), "utf-8");
            Log.d(LOG_TAG, "信息" + output);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return gson.toJson(account);

    }

    private String changeCharset(String text) {
        if (text == null) {
            return null;
        } else {
            try {
                return new String(text.getBytes(), Charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {

        if (mIsChange) {
            new AlertDialogWrapper.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否保存并退出")
                    .setPositiveButton("保存并退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doSaveOrUpdate();
                            EditAccountActivity.super.onBackPressed(); //退出
                        }
                    })
                    .setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BaseApplication.getInstance().showTip("退出");
                            EditAccountActivity.super.onBackPressed();//直接退出
                        }
                    })
                    .setCancelable(true)
                    .show();
        } else {
            EditAccountActivity.super.onBackPressed();//直接退出
        }
    }


    public static class DialogViewHolder {
        @BindView(R.id.img_qrcode)
        ImageView imgView;

        @BindView(R.id.ll_title)
        ViewGroup titleLayout;

        @BindView(R.id.ll_account)
        ViewGroup accountLayout;

        @BindView(R.id.ll_password)
        ViewGroup passwordLayout;

        @BindView(R.id.ll_url)
        ViewGroup urlLayout;

        @BindView(R.id.ll_group)
        ViewGroup groupLayout;

        @BindView(R.id.ll_remark)
        ViewGroup remarkLayout;

        @BindView(R.id.checkbox_title)
        CheckBox titleCheckBox;

        @BindView(R.id.checkbox_account)
        CheckBox accountCheckBox;

        @BindView(R.id.checkbox_password)
        CheckBox passwordCheckBox;

        @BindView(R.id.checkbox_url)
        CheckBox urlCheckBox;

        @BindView(R.id.checkbox_group)
        CheckBox groupCheckBox;

        @BindView(R.id.checkbox_remark)
        CheckBox remarkCheckBox;

        public DialogViewHolder(View root) {
            KnifeKit.bind(this, root);
        }

    }
}
