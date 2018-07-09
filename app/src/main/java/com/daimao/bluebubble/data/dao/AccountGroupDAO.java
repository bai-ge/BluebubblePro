package com.daimao.bluebubble.data.dao;

import android.net.Uri;

public interface AccountGroupDAO {

    int TABLE_DIR = 2;

    public static final String TABLE_NAME = "groupTab";

    public static final String GROUP_NAME = "group_name";

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
     * Message表的URI
     */
    Uri TAB_URI = Uri.withAppendedPath(CONTENT_URI, TABLE_NAME);
}
