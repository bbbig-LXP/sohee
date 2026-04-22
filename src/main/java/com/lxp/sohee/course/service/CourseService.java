package com.lxp.sohee.course.service;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseRepository;
import java.util.List;

public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(String title, String description, Long instructorId, CourseLevel level) {
        Course newCourse = Course.create(title, description, instructorId, level);

        return courseRepository.save(newCourse);
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
    }

    public Course modifyCourse(Long id, String newTItle, String newDescription) {
        Course course = getCourse(id);

        if (newTItle != null && !newTItle.isBlank()) {
            course.updateTitle(newTItle);
        }
        if (newDescription != null && !newDescription.isBlank()) {
            course.updateDescription(newDescription);
        }

        return courseRepository.save(course);
    }

    // 강의 soft delete (status archive로 변경)
    public void archiveCourse(Long id) {
        Course course = getCourse(id);

        course.archive();

        courseRepository.save(course);
    }
}
