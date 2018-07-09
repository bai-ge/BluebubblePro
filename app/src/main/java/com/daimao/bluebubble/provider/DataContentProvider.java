package com.daimao.bluebubble.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daimao.bluebubble.data.dao.AccountDAO;
import com.daimao.bluebubble.data.dao.AccountGroupDAO;
import com.daimao.bluebubble.data.source.local.DBHelper;

public class DataContentProvider extends ContentProvider {

    private static UriMatcher uriMatcher;
    private DBHelper dbHelper = null;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AccountDAO.AUTHORITY, AccountDAO.TABLE_NAME, AccountDAO.TABLE_DIR);
        uriMatcher.addURI(AccountGroupDAO.AUTHORITY, AccountGroupDAO.TABLE_NAME, AccountGroupDAO.TABLE_DIR);

    }

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case AccountDAO.TABLE_DIR:
                cursor = db.query(AccountDAO.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case AccountGroupDAO.TABLE_DIR:
                cursor = db.query(AccountGroupDAO.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        if(cursor!=null){
            //添加通知对象
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case AccountDAO.TABLE_DIR:
                return "vnd.android.cursor.dir/vnd." + AccountDAO.AUTHORITY
                        + AccountDAO.TABLE_NAME;
            case AccountGroupDAO.TABLE_DIR:
                return "vnd.android.cursor.dir/vnd." + AccountGroupDAO.AUTHORITY
                        + AccountGroupDAO.TABLE_NAME;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri resUri = null;
        long rowId ;
        switch (uriMatcher.match(uri)) {
            case AccountDAO.TABLE_DIR:
                rowId = db.insert(AccountDAO.TABLE_NAME, null, contentValues);
                resUri = Uri.parse("content://" + AccountDAO.AUTHORITY + "/" + AccountDAO.TABLE_NAME + "/" + rowId);
                break;
            case AccountGroupDAO.TABLE_DIR:
                rowId = db.insert(AccountGroupDAO.TABLE_NAME, null, contentValues);
                resUri = Uri.parse("content://" + AccountGroupDAO.AUTHORITY + "/" + AccountGroupDAO.TABLE_NAME + "/" + rowId);
                break;
            default:
                break;
        }
        //通知，数据源发生改变
        getContext().getContentResolver().notifyChange(uri, null);
        return resUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)){
            case AccountDAO.TABLE_DIR:
                deleteRow = db.delete(AccountDAO.TABLE_NAME, selection, selectionArgs);
                break;
            case AccountGroupDAO.TABLE_DIR:
                deleteRow = db.delete(AccountGroupDAO.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                break;
        }
        //通知，数据源发生改变
        getContext().getContentResolver().notifyChange(uri,null);
        return deleteRow;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRow = 0;
        switch (uriMatcher.match(uri)){
            case AccountDAO.TABLE_DIR:
                updateRow = db.update(AccountDAO.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case AccountGroupDAO.TABLE_DIR:
                updateRow = db.update(AccountGroupDAO.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                break;
        }
        if(updateRow>0){     //通知，数据源发生改变
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updateRow;
    }
}
