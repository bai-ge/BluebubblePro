package com.daimao.bluebubble.data.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.daimao.bluebubble.data.dao.AccountGroupDAO;
import com.daimao.bluebubble.data.model.AccountGroupEntity;
import com.daimao.bluebubble.listener.DeleteListener;
import com.daimao.bluebubble.listener.QueryListener;
import com.daimao.bluebubble.listener.SaveListener;
import com.daimao.bluebubble.listener.UpdateListener;

public class AccountGroupDAOImpl extends BaseDAOImpl<AccountGroupEntity> implements AccountGroupDAO {



    public static String getSQLCreateTab() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                GROUP_NAME + TEXT_TYPE + ")";
    }

    @Override
    public void putValue(ContentValues values, AccountGroupEntity entity) {
        values.put(_ID, entity.getId());
        values.put(GROUP_NAME, entity.getGroupName());
    }

    @Override
    public AccountGroupEntity newFromCursor(Cursor cursor) {
        AccountGroupEntity accountGroupEntity = new AccountGroupEntity();
        accountGroupEntity.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
        accountGroupEntity.setGroupName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
        return accountGroupEntity;
    }

    @Override
    public Uri getTabUri() {
        return null;
    }

    @Override
    public void doSave(AccountGroupEntity accountGroupEntity, SaveListener listener) {

    }

    @Override
    public void doUpdate(AccountGroupEntity accountGroupEntity, UpdateListener listener) {

    }

    @Override
    public void doDelete(int id, DeleteListener listener) {

    }

    @Override
    public void doFind(int id, QueryListener listener) {

    }
}
