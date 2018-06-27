package com.daimao.bluebubble.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimao.bluebubble.R;
import com.daimao.bluebubble.model.AccountEntity;
import com.daimao.bluebubble.model.AccountGroupEntity;
import com.daimao.bluebubble.util.Tools;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by baige on 2018/6/23.
 */

public class AccountHoverExpandableListAdapter extends AbstractHoverExpandableListAdapter<AccountGroupEntity, AccountEntity> {


    private Context mContext;

    private List<AccountGroupEntity> mGroupList;

    public AccountHoverExpandableListAdapter(Context context, List<AccountGroupEntity> groupEntity) {

        this.mContext = context;
        this.mGroupList = groupEntity;
    }


    @Override
    public int getGroupCount() {
        return mGroupList == null ? 0 : mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        AccountGroupEntity groupEntity = getGroup(groupPosition);
        return groupEntity == null ? 0 : groupEntity.getChildCount();
    }

    @Override
    public AccountGroupEntity getGroup(int groupPosition) {
        return mGroupList == null ? null : mGroupList.get(groupPosition);
    }

    @Override
    public AccountEntity getChild(int groupPosition, int childPosition) {
        AccountEntity item = null;
        if (mGroupList != null && groupPosition >= 0 && groupPosition < mGroupList.size()) {
            AccountGroupEntity groupEntity = mGroupList.get(groupPosition);
            item = groupEntity.getChild(childPosition);
        }
        return item;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(getGroupLayoutId(), parent, false);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        setGroupHolder(holder, groupPosition, isExpanded);  // 加载数据
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(getChildLayoutId(), parent, false);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        setChildHolder(holder, groupPosition, childPosition);  // 加载数据
        return convertView;
    }

    @Override
    public View getHeaderView(int groupPosition, boolean isExpanded, View convertView) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(getGroupLayoutId(), null, false);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        setGroupHolder(holder, groupPosition, isExpanded);  // 加载数据
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void setGroupHolder(final GroupViewHolder holder, int groupPosition, boolean isExpanded) {
        AccountGroupEntity groupItem = mGroupList.get(groupPosition);
        if (groupItem != null) {
            holder.groupNameView.setText(groupItem.getGroupName());
            holder.groupNumView.setText("共" + groupItem.getChildCount() + "项");
        }
        if (isExpanded) {
            holder.imgView.setBackgroundResource(R.drawable.ic_triangle_down);
        } else {
            holder.imgView.setBackgroundResource(R.drawable.ic_triangle_right);
        }
    }

    private void setChildHolder(final ChildViewHolder holder, int groupPosition, int childPosition) {
        AccountEntity item = getChild(groupPosition, childPosition);
        if (item != null) {
            holder.titleView.setText(item.getTitle());
            holder.accountView.setText(item.getAccount());
            holder.addressView.setText(item.getNetworkAddress());
            holder.createTimeView.setText(Tools.formatTime(item.getCreatTime()));

            //TODO 设置图片

        }
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.item_passwordbook_child;
    }

    @Override
    public int getGroupLayoutId() {
        return R.layout.item_passwordbook_group;
    }


    public static class ChildViewHolder {
        @BindView(R.id.parent_layout)
        ViewGroup viewGroup;

        @BindView(R.id.img_logo)
        ImageView imgView;

        @BindView(R.id.txt_title)
        TextView titleView;

        @BindView(R.id.txt_account)
        TextView accountView;

        @BindView(R.id.txt_address)
        TextView addressView;

        @BindView(R.id.txt_create_time)
        TextView createTimeView;

//        @BindView(R.id.linear_checkbox)
//        ViewGroup checkboxGroup;
//
//        @BindView(R.id.checkbox)
//        CheckBox checkBox;

        public ChildViewHolder(View view) {
            KnifeKit.bind(this, view);
        }
    }

    public static class GroupViewHolder {
        @BindView(R.id.parent_layout)
        ViewGroup viewGroup;

        @BindView(R.id.img_group)
        ImageView imgView;

        @BindView(R.id.txt_group_name)
        TextView groupNameView;

        @BindView(R.id.txt_group_num)
        TextView groupNumView;

        public GroupViewHolder(View view) {
            KnifeKit.bind(this, view);
        }
    }
}
