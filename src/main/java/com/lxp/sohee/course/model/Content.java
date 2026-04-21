package com.lxp.sohee.course.model;

import java.time.LocalDateTime;

public class Content {
    private Long id;
    private CourseSection section;
    private String title;
    private ContentType type;
    private ContentStatus status;
    private LocalDateTime createdAt;

    public ContentStatus getStatus() {
        return status;
    }

    public CourseSection getSection() {
        return section;
    }

    private LocalDateTime updatedAt;
}
