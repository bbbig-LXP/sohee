package com.lxp.sohee.course.controller;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.service.CourseService;
import java.util.ArrayList;
import java.util.List;

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

    // 강의 전체 목록 조회
    public void list() {
        try {
            List<Course> courses = courseService.findAllCourses();

            if(courses.isEmpty()) {
                System.out.println("현재 등록된 강의가 없습니다.");
                return;
            }

            for (Course course : courses) {
                System.out.println("ID: " + course.getId() + " | 제목: " + course.getTitle() + " | 강사ID: " + course.getInstructorId());
            }
        } catch (Exception e) {
            System.out.println("목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 강의 ID로 강의 1개 조회
    public void detail(Long id) {
        try {
            Course course = courseService.getCourse(id);

            System.out.println("=== 강의 상세 정보 ===");
            System.out.println("ID: " + course.getId());
            System.out.println("제목: " + course.getTitle());
            System.out.println("설명: " + course.getDescription());
            System.out.println("강사 ID: " + course.getInstructorId());
            System.out.println("상태: " + course.getStatus());
            System.out.println("난이도: " + course.getLevel());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 강의 (title, description) 수정
    public void update(Long id, String newTitle, String newDescription) {
        try {
            courseService.modifyCourse(id, newTitle, newDescription);
            System.out.println("강좌 정보가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("수정 불가: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("시스템 오류: " + e.getMessage());
        }
    }

}
