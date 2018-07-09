package com.daimao.bluebubble.listener;

import com.daimao.bluebubble.exception.SqlException;

import java.util.List;

public abstract class QueryListListener<T> {

    public abstract void done(List<T> list, SqlException e);
}
