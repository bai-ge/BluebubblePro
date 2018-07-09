package com.daimao.bluebubble.data.dao.impl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.data.dao.AccountDAO;
import com.daimao.bluebubble.data.model.AccountEntity;
import com.daimao.bluebubble.exception.SqlException;
import com.daimao.bluebubble.listener.DeleteListener;
import com.daimao.bluebubble.listener.QueryListener;
import com.daimao.bluebubble.listener.SaveListener;
import com.daimao.bluebubble.listener.UpdateListener;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;
import static com.daimao.bluebubble.util.Tools.checkNotNull;

public class AccountDAOImpl extends BaseDAOImpl<AccountEntity> implements AccountDAO {


    public static String getSQLCreateTab() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                IS_LOCK + INTEGER_TYPE + COMMA_SEP +
                LOGO_PATH + TEXT_TYPE + COMMA_SEP +
                TITLE + TEXT_TYPE + COMMA_SEP +
                ACCOUNT + TEXT_TYPE + COMMA_SEP +
                PASSWORD + TEXT_TYPE + COMMA_SEP +
                GROUP_NAME + TEXT_TYPE + COMMA_SEP +
                URL + TEXT_TYPE + COMMA_SEP +
                CREAT_TIME + INTEGER_TYPE + COMMA_SEP +
                REMARK + TEXT_TYPE +")";
    }

    @Override
    public void putValue(ContentValues values, AccountEntity entity) {
        checkNotNull(values);
        values.put(_ID, entity.getId());
        values.put(IS_LOCK, entity.isLock() ? TRUE : FALSE);
        values.put(LOGO_PATH, entity.getLogoPath());
        values.put(TITLE, entity.getTitle());
        values.put(ACCOUNT, entity.getAccount());
        values.put(PASSWORD, entity.getPassword());
        values.put(GROUP_NAME, entity.getGroupName());
        values.put(URL,entity. getUrl());
        values.put(CREAT_TIME, entity.getCreatTime());
        values.put(REMARK, entity.getRemark());
    }

    @Override
    public AccountEntity newFromCursor(Cursor cursor) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
        accountEntity.setLock(cursor.getInt(cursor.getColumnIndex(IS_LOCK)) == TRUE);
        accountEntity.setLogoPath(cursor.getString(cursor.getColumnIndex(LOGO_PATH)));
        accountEntity.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        accountEntity.setAccount(cursor.getString(cursor.getColumnIndex(ACCOUNT)));
        accountEntity.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        accountEntity.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
        accountEntity.setGroupName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
        accountEntity.setCreatTime(cursor.getLong(cursor.getColumnIndex(CREAT_TIME)));
        accountEntity.setRemark(cursor.getString(cursor.getColumnIndex(REMARK)));
        return accountEntity;
    }

    @Override
    public Uri getTabUri() {
        return TAB_URI;
    }

    @Override
    public void doSave(AccountEntity accountEntity, SaveListener listener) {
        ContentValues contentValues = new ContentValues();
        putValue(contentValues, accountEntity);
        contentValues.remove(_ID);
        ContentResolver contentResolver = BaseApplication.getAppContext().getContentResolver();
        if(contentResolver != null){
            Uri uri = contentResolver.insert(getTabUri(), contentValues);
            int row = (int) ContentUris.parseId(uri);
            accountEntity.setId(row);
            Log.d(LOG_TAG, "doSave()" + row);
            listener.done(accountEntity, new SqlException(SqlException.SUCCESS));
        }else{
            listener.done(accountEntity, new SqlException(SqlException.FAIL));
        }
    }

    @Override
    public void doUpdate(AccountEntity accountEntity, UpdateListener listener) {
        ContentValues contentValues = new ContentValues();
        putValue(contentValues, accountEntity);
        ContentResolver contentResolver = BaseApplication.getAppContext().getContentResolver();
        if(contentResolver != null){
            String selection = _ID + "= ?";
            String[] selectionArgs = { String.valueOf(accountEntity.getId()) };
            int updateRow = contentResolver.update(getTabUri(), contentValues, selection, selectionArgs );
            Log.d(LOG_TAG, "doUpdate()"+updateRow);
            listener.done(new SqlException(SqlException.SUCCESS));
        }else{
            listener.done(new SqlException(SqlException.FAIL));
        }
    }

    @Override
    public void doDelete(int id, DeleteListener listener) {
        ContentResolver contentResolver = BaseApplication.getAppContext().getContentResolver();
        if(contentResolver != null){
            String selection = _ID + "= ?";
            String[] selectionArgs = {String.valueOf(id)};
            int deleteRow = contentResolver.delete(getTabUri(), selection, selectionArgs);
            Log.d(LOG_TAG, "doDelete()"+deleteRow);
            listener.done(new SqlException(SqlException.SUCCESS));
        }else{
            listener.done(new SqlException(SqlException.FAIL));
        }
    }

    @Override
    public void doFind(int id, QueryListener listener) {

    }
}
