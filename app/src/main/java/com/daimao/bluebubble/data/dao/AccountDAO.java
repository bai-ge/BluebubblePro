package com.daimao.bluebubble.data.dao;


import android.net.Uri;

public interface AccountDAO {

    int TABLE_DIR = 1;
    /*
     * 字段
     *
     * */
    public static final String TABLE_NAME = "accountTab";

    public static final String IS_LOCK = "is_lock";

    public static final String LOGO_PATH = "logo_path";

    public static final String TITLE = "title";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String GROUP_NAME = "group_name";

    public static final String URL = "url";

    public static final String CREAT_TIME = "creat_time";

    public static final String REMARK = "remark";

    /*
     * ContentProvider的authorities
     * */

    String AUTHORITY = "com.daimao.bluebubble.provider.DataContentProvider";

    /**
     * Scheme
     */
    String SCHEME = "content";

    /**
     * ContentProvider的URI
     */
    Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    /**
     * 表的URI
     */
    Uri TAB_URI = Uri.withAppendedPath(CONTENT_URI, TABLE_NAME);


}
