package com.lxp.sohee.course.model;

import java.time.LocalDateTime;

public class CourseSection {
    private Long id;
    private Course course;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }
}
