package com.daimao.bluebubble.data.source.local.dbupdate;

import android.database.sqlite.SQLiteDatabase;


@VersionCode(2)
public class VersionSecond extends Upgrade {
    @Override
    public void update(SQLiteDatabase db) {
        //TODO
        //第一步将表A重命名为temp_A
        //第二步创建新表A,此时表结构已加了2列
        //第三步将temp_A表中的数据插入到表A
        //第四步删除临时表temp_A
    }
}
