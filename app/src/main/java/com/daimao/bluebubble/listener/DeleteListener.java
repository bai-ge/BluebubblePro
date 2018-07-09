package com.daimao.bluebubble.listener;

import com.daimao.bluebubble.exception.SqlException;

public abstract class DeleteListener {

    public abstract void done(SqlException e);
}
