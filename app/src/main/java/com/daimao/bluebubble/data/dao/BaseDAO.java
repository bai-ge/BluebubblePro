package com.daimao.bluebubble.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.daimao.bluebubble.listener.DeleteListener;
import com.daimao.bluebubble.listener.QueryListener;
import com.daimao.bluebubble.listener.SaveListener;
import com.daimao.bluebubble.listener.UpdateListener;

public interface BaseDAO<T> extends BaseColumns {

    String TEXT_TYPE = " TEXT";

    String INTEGER_TYPE = " INTEGER";

    String COMMA_SEP = ",";

    int TRUE = 1;

    int FALSE = 0;


    void putValue(ContentValues values, T entity);

    T newFromCursor(Cursor cursor);

    Uri getTabUri();

    void doSave(T t, SaveListener listener);

    void doUpdate(T t, UpdateListener listener);

    void doDelete(int id, DeleteListener listener);

    void doFind(int id, QueryListener listener);

}
