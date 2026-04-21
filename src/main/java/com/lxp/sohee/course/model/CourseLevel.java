package com.lxp.sohee.course.model;

public enum CourseLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    public boolean isBeginner() {
        return this == BEGINNER;
    }

    public boolean isIntermediate() {
        return this == INTERMEDIATE;
    }

    public boolean isAdvanced() {
        return this == ADVANCED;
    }
}
