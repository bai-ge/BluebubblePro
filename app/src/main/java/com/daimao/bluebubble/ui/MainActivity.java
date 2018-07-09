package com.daimao.bluebubble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.data.model.AccountEntity;
import com.daimao.bluebubble.fragment.NotebookFragment;
import com.daimao.bluebubble.fragment.PasswordBookFragment;
import com.daimao.bluebubble.fragment.PersonalFragment;
import com.daimao.bluebubble.fragment.ToolsFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;


public class MainActivity extends XActivity implements ViewPager.OnPageChangeListener, BottomNavigationBar.OnTabSelectedListener{

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    List<Fragment> fragmentList = new ArrayList<>();

    String[] titles = {"密码本", "记事本", "工具", "个人"};

    XFragmentAdapter adapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
//getContentResolver().insert()

    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("密码本");
//        getSupportActionBar().setShowHideAnimationEnabled(true);

        fragmentList.clear();
        fragmentList.add(PasswordBookFragment.newInstance());
        fragmentList.add(NotebookFragment.newInstance());
        fragmentList.add(ToolsFragment.newInstance());
        fragmentList.add(PersonalFragment.newInstance());

        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);
        mBottomNavigationBar.setInActiveColor(R.color.dark_gray);
        mBottomNavigationBar.setActiveColor(R.color.blue);



//        mBadgeItem = new BadgeItem();
//        mBadgeItem.setHideOnSelect(false)
//                .setText("10")
//                .setBackgroundColorResource(R.color.orange)
//                .setBorderWidth(0);

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_passwordbook, titles[0]))
                .addItem(new BottomNavigationItem(R.drawable.ic_notebook, titles[1]))
                .addItem(new BottomNavigationItem(R.drawable.ic_deedbox, titles[2]))
                .addItem(new BottomNavigationItem(R.drawable.ic_person, titles[3]))
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);


    }

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.menu_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
//                AboutActivity.launch(context);
                BaseApplication.getInstance().showTip("帮助");
                break;
            case R.id.menu_add_pwd:
                Router.newIntent(MainActivity.this).to(EditAccountActivity.class).putInt("add", 1).launch();
                break;
            case R.id.menu_change_lock:
                Router.newIntent(MainActivity.this).to(ChangeLockActivity.class).launch();
                break;
            case R.id.menu_qrcode:
                BaseApplication.getInstance().showTip("二维码");
                Router.newIntent(MainActivity.this).to(QRCodeActivity.class).launch();
                break;
            case R.id.menu_qrcode_recognize:
                customScan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object newP() {
        return null;
    }



    // 你也可以使用简单的扫描功能，但是一般扫描的样式和行为都是可以自定义的，这里就写关于自定义的代码了
// 你可以把这个方法作为一个点击事件
    public void customScan() {
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setPrompt("扫描二维码")
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
                AccountEntity accountEntity = AccountEntity.newFromQRCode(scanResult);
                if(accountEntity != null){
                    Router.newIntent(MainActivity.this).to(EditAccountActivity.class).putParcelable("account", accountEntity).putInt("add", 1).launch();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private long clickBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long now = System.currentTimeMillis();
            if (now - clickBackTime < 500) {
                BaseApplication.getInstance().finishAll();
            } else {
                Log.i(LOG_TAG, "KEYCODE_BACK");
                clickBackTime = now;
                BaseApplication.getInstance().showTip("再按一次返回键退出");
            }
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    public void onTabSelected(int position) {
        getSupportActionBar().setTitle(titles[position]);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setTitle(titles[position]);
        mBottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
