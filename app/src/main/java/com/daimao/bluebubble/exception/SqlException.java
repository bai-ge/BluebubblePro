package com.daimao.bluebubble.exception;

public class SqlException extends Exception{

    public final static int SUCCESS = 1000;

    public final static int FAIL = 1001;

    private int code;

    public SqlException() {
        super();
    }

    public SqlException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SqlException(String detailMessage) {
        super(detailMessage);
    }

    public SqlException(Throwable throwable) {
        super(throwable);
    }

    public SqlException(int exceptionCode) {
        this.code = exceptionCode;
    }

    public int getErrorCode() {
        return this.code;
    }

    public String toString() {
        return "errorCode:" + this.code + ",errorMsg:" + this.getMessage();
    }
}
