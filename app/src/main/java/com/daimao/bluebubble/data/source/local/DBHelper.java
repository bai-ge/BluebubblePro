package com.daimao.bluebubble.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daimao.bluebubble.data.dao.impl.AccountDAOImpl;
import com.daimao.bluebubble.data.dao.impl.AccountGroupDAOImpl;
import com.daimao.bluebubble.data.model.AccountEntity;
import com.daimao.bluebubble.data.model.AccountGroupEntity;
import com.daimao.bluebubble.data.source.local.dbupdate.Upgrade;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bluebubble.db";

    private static final int DATABASE_VERSION = VersionFactory.getCurrentDBVersion();

    private static volatile DBHelper instance = null;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null){
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建账号表
        sqLiteDatabase.execSQL(AccountDAOImpl.getSQLCreateTab());

        //创建分组表
        sqLiteDatabase.execSQL(AccountGroupDAOImpl.getSQLCreateTab());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        update(db, oldVersion, newVersion);
    }

    /**
     * 数据库版本递归更新
     *
     * @param oldVersion 数据库当前版本号
     * @param newVersion 数据库升级后的版本号
     * @author lh
     * @retrun void
     */
    public static void update(SQLiteDatabase db, int oldVersion, int newVersion) {
        Upgrade upgrade = null;
        if (oldVersion < newVersion) {
            oldVersion++;
            upgrade = VersionFactory.getUpgrade(oldVersion);
            if (upgrade == null) {
                return;
            }
            upgrade.update(db);
            update(db, oldVersion, newVersion);
        }
    }

}
