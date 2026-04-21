package com.lxp.sohee.course.controller;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.service.CourseService;

public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 강의 생성
    public void addCourse(String title, String description, Long instructorId, CourseLevel level) {
        try {
            Course savedCourse = courseService.createCourse(title, description, instructorId, level);
            System.out.println("강의 등록 성공! (ID: " + savedCourse.getId() + ")");
        } catch (Exception e) {
            System.out.println("강의 등록 실패: " + e.getMessage());
        }
    }

}
