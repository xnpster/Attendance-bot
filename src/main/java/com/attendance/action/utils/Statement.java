package com.attendance.action.utils;

public abstract class Statement {
    public String statement;

    protected Statement(String s) {
        statement = s;
    }

    public abstract boolean isStrong();
}
