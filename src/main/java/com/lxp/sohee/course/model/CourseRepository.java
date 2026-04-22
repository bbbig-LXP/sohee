package com.lxp.sohee.course.model;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course save(Course course); // 강좌 생성 및 수정

    List<Course> findAll(); // 모든 강좌 조회

    Optional<Course> findById(Long id); // 강좌 ID로 강좌 1개 조회

    boolean existCourseId(Long id); // ID로 강의 존재 여부 확인
}
