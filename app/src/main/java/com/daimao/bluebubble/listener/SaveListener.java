package com.daimao.bluebubble.listener;

import com.daimao.bluebubble.exception.SqlException;

public abstract class SaveListener<T> {

    public abstract void done(T entity, SqlException var2);
}
