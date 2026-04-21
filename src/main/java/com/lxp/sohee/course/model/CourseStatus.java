package com.lxp.sohee.course.model;

public enum CourseStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED;

    public boolean isDraft() {
        return this == DRAFT;
    }

    public boolean isPublished() {
        return this == PUBLISHED;
    }

    public boolean isArchived() {
        return this == ARCHIVED;
    }
}