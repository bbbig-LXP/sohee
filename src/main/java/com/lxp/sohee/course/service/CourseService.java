package com.lxp.sohee.course.service;

import com.lxp.sohee.course.model.Content;
import com.lxp.sohee.course.model.ContentRepository;
import com.lxp.sohee.course.model.ContentStatus;
import com.lxp.sohee.course.model.ContentType;
import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseRepository;
import com.lxp.sohee.course.model.CourseSection;
import com.lxp.sohee.course.model.CourseSectionRepository;
import java.util.List;

public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final ContentRepository contentRepository;

    public CourseService(CourseRepository courseRepository, CourseSectionRepository courseSectionRepository, ContentRepository contentRepository) {
        this.courseRepository = courseRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.contentRepository = contentRepository;
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

    // 강좌 섹션 추가
    public void addSection(Long courseId, String title) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));

        CourseSection newSection = CourseSection.create(course, title);
        course.addSection(newSection);

        courseSectionRepository.save(newSection);
    }

    // 컨텐츠 추가
    public void addContent(Long sectionId, String title, ContentType type, ContentStatus status) {
        CourseSection section = courseSectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 섹션입니다."));

        Content newContent = Content.create(section, title, type, status);

        section.addContent(newContent);

        contentRepository.save(newContent);
    }
}
