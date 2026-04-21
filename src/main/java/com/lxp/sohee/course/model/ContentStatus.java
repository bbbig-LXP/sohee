package com.lxp.sohee.course.model;

public enum ContentStatus {
    NORMAL,
    HIDDEN;

    public boolean isNormal() {
        return this == NORMAL;
    }
}
