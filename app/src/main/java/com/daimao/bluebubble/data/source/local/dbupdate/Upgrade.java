package com.daimao.bluebubble.data.source.local.dbupdate;


import android.database.sqlite.SQLiteDatabase;

public abstract class Upgrade {

    public abstract void update(SQLiteDatabase db);
}