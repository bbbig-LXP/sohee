package com.lxp.sohee.course.model;

import java.util.Optional;

public interface CourseSectionRepository {
    CourseSection save(CourseSection section); // 강좌 섹션 생성

    Optional<CourseSection> findById(Long id); // 섹션 ID로 1개 조회
}
