package com.lxp.sohee.course.model;

import java.time.LocalDateTime;
import java.util.List;

public class Course {
    private Long id;
    private String title;
    private String description;
    private Long instructorId;
    private CourseStatus status;
    private CourseLevel level;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Course() {}

    private Course(String title, String description, Long instructorId, CourseLevel level) {
        validateTitle(title);
        validateDescription(description);
        validateInstructorId(instructorId);
        validateLevel(level);

        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.status = CourseStatus.DRAFT;
        this.level = level;
        this.publishedAt = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("강의 제목은 필수입니다.");
        }
        if (title.length() < 2 || title.length() > 50) {
            throw new IllegalArgumentException("강의 제목은 2자 이상 50자 이하로 작성해야 합니다.");
        }
    }

    public void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("강의 설명은 필수입니다.");
        }
        if (description.length() < 2 || description.length() > 200) {
            throw new IllegalArgumentException("강의 설명은 2자 이상 200자 이하로 작성해야 합니다.");
        }
    }

    public void validateInstructorId(Long instructorId) {
        if (instructorId == null) {
            throw new IllegalArgumentException("강의자 ID는 필수값입니다.");
        }
    }

    public void validateLevel(CourseLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("강의 레벨은 필수값입니다.");
        }
    }

    /*
    * 강좌 생성
    * - 생성자에서 유효값 검증 후 Course 객체를 생성한다
    * */
    public static Course create(String title, String description, Long instructorId, CourseLevel level) {
        return new Course(title, description, instructorId, level);
    }

    /*
    * 강좌 조회
    * - DB에 저장된 값을 Course 객체로 복원한다.
    * */
    public static Course reconstruct(Long id, String title, String description, Long instructorId,
            CourseStatus status, CourseLevel level, LocalDateTime publishedAt,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        Course course = new Course();

        course.id = id;
        course.title = title;
        course.description = description;
        course.instructorId = instructorId;
        course.status = status;
        course.level = level;
        course.publishedAt = publishedAt;
        course.createdAt = createdAt;
        course.updatedAt = updatedAt;

        return course;
    }

    /*
    * 강좌 제목 변경
    * - 강좌 status가 DRAFT, PUBLISHED 인 경우에만 변경 가능
    * - 새로운 제목이 유효값인지 검증 후 title과 updatedAt을 변경한다.
    * */
    public void updateTitle(String newTitle) {
        if (this.status == CourseStatus.ARCHIVED) {
            throw new IllegalArgumentException("보관되어 있는 상태의 강의는 제목을 변경할 수 없습니다.");
        }

        validateTitle(newTitle);
        this.title = newTitle;
        this.updatedAt = LocalDateTime.now();
    }

    /*
    * 강좌 설명 변경
    * */
    public void updateDescription(String newDescription) {
        if (this.status == CourseStatus.ARCHIVED) {
            throw new IllegalArgumentException("보관되어 있는 상태의 강의는 제목을 변경할 수 없습니다.");
        }

        validateDescription(newDescription);
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    /*
    * 강좌 ARCHIVED 상태 변경
    * - soft delete를 상태 변경 메서드로 구현
    * - 현재 status가 ARCHIVED인지 확인
    * - 아니라면 status와 updatedAt을 변경
    * */
    public void archive() {
        if (this.status == CourseStatus.ARCHIVED) {
            throw new IllegalArgumentException("이미 보관 처리된 강의입니다.");
        }

        this.status = CourseStatus.ARCHIVED;
        this.updatedAt = LocalDateTime.now();
    }


    /*
    * 강좌 발행
    * - status가 DRAFT일 때만 발행 가능
    * - 강좌 내 섹션이 최소 1개 이상 존재, 각 센셕의 콘텐츠가 최소 1개 이상 존재해야 한다
    * - 검증 통과 시 status를 PUBLISHED로 변경, pushedAt을 현재 시각으로 변경
    * */
    public void publish(List<CourseSection> sections, List<Content> contents) {
        if (this.status != CourseStatus.DRAFT) {
            throw new IllegalArgumentException("강의 상태가 DRAFT인 경우에만 발행 가능합니다.");
        }
        if (sections == null || sections.isEmpty()) {
            throw new IllegalArgumentException("강의 섹션이 없는 강의는 발행할 수 없습니다.");
        }

        for (CourseSection section : sections) {
            boolean hasNormalContent = contents.stream()
                    .filter(c -> c.getSection().getId().equals(section.getId()))
                    .anyMatch(c -> c.getStatus().isNormal());

            if (!hasNormalContent) {
                throw new IllegalArgumentException("섹션은 최소 1개 이상의 정상 콘텐츠를 포함해야 합니다.");
            }
        }

        this.status = CourseStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

}
