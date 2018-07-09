package com.daimao.bluebubble.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;

import com.daimao.bluebubble.AppConfigure;
import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.adapter.AccountHoverExpandableListAdapter;
import com.daimao.bluebubble.data.dao.AccountDAO;
import com.daimao.bluebubble.data.dao.impl.AccountDAOImpl;
import com.daimao.bluebubble.data.model.AccountEntity;
import com.daimao.bluebubble.data.model.AccountGroupEntity;
import com.daimao.bluebubble.ui.EditAccountActivity;
import com.daimao.bluebubble.view.HoverExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * Created by baige on 2018/6/18.
 */

public class PasswordBookFragment extends XFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.explistview)
    HoverExpandableListView mExplistview;

    @BindView(R.id.layout_null)
    ViewGroup mNothingView;

    private AccountHoverExpandableListAdapter mAdapter;

    private List<AccountGroupEntity> mAccountGroupEntityList = new ArrayList<AccountGroupEntity>(){
        @Override
        public int indexOf(Object o) {
            if (o == null) {
                for (int i = 0; i < this.size(); i++)
                    if (this.get(i)==null)
                        return i;
            } else {
                for (int i = 0; i < this.size(); i++)
                    if (this.get(i)!= null && this.get(i).equals(o))
                        return i;
            }
            return -1;
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
//        for (int i = 0; i < 5; i++) {
//            AccountGroupEntity groupEntity = new AccountGroupEntity();
//            groupEntity.setGroupName("group " + i);
//            ArrayList<AccountEntity> list = new ArrayList<>();
//            for (int j = 0; j < 10; j++) {
//                AccountEntity accountEntity = new AccountEntity();
//                accountEntity.setTitle(groupEntity.getGroupName() + "item " + j);
//                accountEntity.setCreatTime(System.currentTimeMillis());
//                accountEntity.setAccount(AppConfigure.LOG_TAG);
//                accountEntity.setUrl("www.baidu.com");
//                list.add(accountEntity);
//            }
//            groupEntity.setChildList(list);
//            mAccountGroupEntityList.add(groupEntity);
//        }

        mAdapter = new AccountHoverExpandableListAdapter(getContext(), mAccountGroupEntityList);
        mAdapter.setListener(mHoverExpandableListener);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(mAdapter.getGroupCount() == 0){
                    mNothingView.setVisibility(View.VISIBLE);
                }else{
                    mNothingView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });
        mExplistview.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, PasswordBookFragment.this);

//        AccountDAOImpl accountDAO = new AccountDAOImpl();
//
//        AccountEntity accountEntity = new AccountEntity();
//        accountEntity.setGroupName("group 0");
//        accountEntity.setTitle("group 0"  + "item 0");
//        accountEntity.setCreatTime(System.currentTimeMillis());
//        accountEntity.setAccount(AppConfigure.LOG_TAG);
//        accountEntity.setUrl("www.baidu.com");
//        ContentValues contentValues = new ContentValues();
//        accountDAO.putValue(contentValues, accountEntity);
//        contentValues.remove(AccountDAOImpl._ID);
//        accountDAO.doSave(accountEntity, );
//        getActivity().getContentResolver().insert(AccountDAO.TAB_URI,  contentValues);
//        getContext().getContentResolver().insert(AccountDAO.TAB_URI, contentValues);


    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_passwordbook;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static PasswordBookFragment newInstance() {
        return new PasswordBookFragment();
    }

    @Override
    public void onDestroy() {
        //销毁loader,释放资源
        getLoaderManager().destroyLoader(LOADER_ID);
        super.onDestroy();
    }

    /**
     * 加载器的标示
     */
    private final int LOADER_ID = 1;


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(getContext(), AccountDAO.TAB_URI, null, null, null, null);
        }
        return null;
    }

    /**
     * 首次加载成功或者数据源发生改变时候进行回调
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_ID) { //实时刷新数据到UI上，这里，Cursor对象会自动被关闭
            if (data != null && data.moveToFirst()) {
                List<AccountEntity> list = new ArrayList<>();
                AccountDAOImpl accountDAO = new AccountDAOImpl();
                do {
                    list.add(accountDAO.newFromCursor(data));
                } while (data.moveToNext());

                initAdapter(list);
            }
        }
    }

    private void initAdapter(List<AccountEntity> accountEntityList){
        mAccountGroupEntityList.clear();
        for (AccountEntity accountEntity : accountEntityList){
            AccountGroupEntity accountGroupEntity = null;
            int index = mAccountGroupEntityList.indexOf(accountEntity.getGroupName());
            if(index >= 0){
                accountGroupEntity = mAccountGroupEntityList.get(index);
                accountGroupEntity.getChildList().add(accountEntity);
            }else{
                List<AccountEntity> list = new ArrayList<>();
                list.add(accountEntity);
                accountGroupEntity = new AccountGroupEntity();
                accountGroupEntity.setGroupName(accountEntity.getGroupName());
                accountGroupEntity.setChildList(list);
                mAccountGroupEntityList.add(accountGroupEntity);
            }
        }
        mAdapter.updateList(mAccountGroupEntityList);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private AccountHoverExpandableListAdapter.OnHoverExpandableListener<AccountGroupEntity, AccountEntity> mHoverExpandableListener = new AccountHoverExpandableListAdapter.OnHoverExpandableListener<AccountGroupEntity, AccountEntity>() {
        @Override
        public void OnClickGroup(View view, AccountGroupEntity accountGroupEntity) {

        }

        @Override
        public boolean OnLongClickGroup(View view, AccountGroupEntity accountGroupEntity) {
            return false;
        }

        @Override
        public void OnClickItem(View view, AccountGroupEntity accountGroupEntity, AccountEntity accountEntity) {
            BaseApplication.getInstance().showTip("点击"+accountEntity.getId());
            Router.newIntent(getActivity())
                    .to(EditAccountActivity.class)
                    .putParcelable("account", accountEntity)
                    .launch();
        }

        @Override
        public boolean OnLongClickItem(View view, AccountGroupEntity accountGroupEntity, AccountEntity accountEntity) {
            return false;
        }
    };
}
