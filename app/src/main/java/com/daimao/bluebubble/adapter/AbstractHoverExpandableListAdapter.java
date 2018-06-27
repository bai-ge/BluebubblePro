package com.daimao.bluebubble.adapter;

import android.view.View;
import android.widget.BaseExpandableListAdapter;

/**
 * Created by baige on 2018/6/23.
 */

public abstract class AbstractHoverExpandableListAdapter<G, T> extends BaseExpandableListAdapter {

    /**返回当前悬浮的视图
     * @param groupPosition
     * @return
     */
    public abstract View getHeaderView(int groupPosition, boolean isExpanded, View convertView);

    public abstract G getGroup(int groupPosition);

    public abstract T getChild(int groupPosition, int childPosition);

    public abstract int  getChildLayoutId();

    public abstract int  getGroupLayoutId();
}
