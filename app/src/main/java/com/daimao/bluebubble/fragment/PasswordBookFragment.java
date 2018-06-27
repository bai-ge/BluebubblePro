package com.daimao.bluebubble.fragment;

import android.os.Bundle;

import com.daimao.bluebubble.AppConfigure;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.adapter.AccountHoverExpandableListAdapter;
import com.daimao.bluebubble.model.AccountEntity;
import com.daimao.bluebubble.model.AccountGroupEntity;
import com.daimao.bluebubble.view.HoverExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * Created by baige on 2018/6/18.
 */

public class PasswordBookFragment extends XFragment {

    @BindView(R.id.explistview)
    HoverExpandableListView mExplistview;

    private AccountHoverExpandableListAdapter mAdapter;

    private List<AccountGroupEntity> mAccountGroupEntityList;

    @Override
    public void initData(Bundle savedInstanceState) {
        mAccountGroupEntityList = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            AccountGroupEntity groupEntity = new AccountGroupEntity();
            groupEntity.setGroupName("group "+i);
            ArrayList<AccountEntity> list = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                AccountEntity accountEntity = new AccountEntity();
                accountEntity.setTitle(groupEntity.getGroupName()+"item "+j);
                accountEntity.setCreatTime(System.currentTimeMillis());
                accountEntity.setAccount(AppConfigure.LOG_TAG);
                accountEntity.setNetworkAddress("www.baidu.com");
                list.add(accountEntity);
            }
            groupEntity.setChildList(list);
            mAccountGroupEntityList.add(groupEntity);
        }
        mAdapter = new AccountHoverExpandableListAdapter(getContext(), mAccountGroupEntityList);
        mExplistview.setAdapter(mAdapter);
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
}
