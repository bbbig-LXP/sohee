package com.lxp.sohee.course.service;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseRepository;

public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(String title, String description, Long instructorId, CourseLevel level) {
        Course newCourse = Course.create(title, description, instructorId, level);

        return courseRepository.save(newCourse);
    }
}
