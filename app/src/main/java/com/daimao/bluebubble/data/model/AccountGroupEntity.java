package com.daimao.bluebubble.data.model;

import android.content.ContentValues;

import java.util.List;
import java.util.Objects;

import static com.daimao.bluebubble.util.Tools.checkNotNull;

/**
 * Created by baige on 2018/6/22.
 */

public class AccountGroupEntity  {
    int id;
    boolean isExpanded;
    String groupName;
    List<AccountEntity> childList;

    public static final String TABLE_NAME = "groupTab";

    public static final String GROUP_NAME = "group_name";



    public int getChildCount(){
        return childList == null ? 0 : childList.size();
    }

    public AccountEntity getChild(int pos){
        if(pos >= 0 && pos < getChildCount()){
            return childList.get(pos);
        }
        return null;
    }

//    @Override
//    public void putValue(ContentValues values) {
//        checkNotNull(values);
//        values.put(_ID, getId());
//        values.put(GROUP_NAME, getGroupName());
//    }
//
//
//    public static String getSQLCreateTab() {
//        return "CREATE TABLE " + TABLE_NAME + " (" +
//                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
//                GROUP_NAME + TEXT_TYPE + COMMA_SEP + ")";
//    }

    //get and set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<AccountEntity> getChildList() {
        return childList;
    }

    public void setChildList(List<AccountEntity> childList) {
        this.childList = childList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if(o instanceof String ){
            return Objects.equals(groupName, o);
        }
        if(getClass() != o.getClass()){
            return false;
        }
        AccountGroupEntity that = (AccountGroupEntity) o;
        return id == that.id &&
                isExpanded == that.isExpanded &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(childList, that.childList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }
}
