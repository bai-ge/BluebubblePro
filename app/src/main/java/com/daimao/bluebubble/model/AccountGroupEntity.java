package com.daimao.bluebubble.model;

import java.util.List;

/**
 * Created by baige on 2018/6/22.
 */

public class AccountGroupEntity {
    int id;
    boolean isExpanded;
    String groupName;
    List<AccountEntity> childList;

    public int getChildCount(){
        return childList == null ? 0 : childList.size();
    }

    public AccountEntity getChild(int pos){
        if(pos >= 0 && pos < getChildCount()){
            return childList.get(pos);
        }
        return null;
    }

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


}
