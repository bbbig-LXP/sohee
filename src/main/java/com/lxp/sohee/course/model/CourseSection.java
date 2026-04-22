package com.lxp.sohee.course.model;

import java.time.LocalDateTime;

public class CourseSection {
    private Long id;
    private Course course;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CourseSection() {}

    private CourseSection(Course course, String title) {
        validateTitle(title);

        this.course = course;
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    public void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("섹션 제목은 필수입니다.");
        }
        if (title.length() < 2 || title.length() > 50) {
            throw new IllegalArgumentException("섹션 제목은 2자 이상 50자 이하여야 합니다.");
        }
    }

    /*
    * 강좌 섹션 생성
    * - 생성자에서 유효값 검증 후 CourseSection 객체를 생성한다
    * */
    public CourseSection create(Course course, String title) {
        return new CourseSection(course, title);
    }

    /*
    * 강좌 섹션 조회
    * - DB에 저장된 값을 CourseSection 객체로 복원한다.
    * - 유효값 검증된 객체를 DB에 저장하였고, 그걸 복원하는 과정이므로 빈 생성자를 통해 CourseSection 객체를 생성한다
    * */
    public CourseSection reconstruct(Long id, Course course, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        CourseSection section = new CourseSection();

        section.id = id;
        section.course = course;
        section.title = title;
        section.createdAt = createdAt;
        section.updatedAt = updatedAt;
        
        return section;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }
}
