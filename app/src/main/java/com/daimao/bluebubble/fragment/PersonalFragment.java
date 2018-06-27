package com.daimao.bluebubble.fragment;

import android.os.Bundle;

import com.daimao.bluebubble.R;

import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * Created by baige on 2018/6/18.
 */

public class PersonalFragment extends XFragment {
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_personal;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }
}
