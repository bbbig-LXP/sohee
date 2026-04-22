package com.lxp.sohee.course.model;

import java.time.LocalDateTime;

public class Content {
    private Long id;
    private CourseSection section;
    private String title;
    private ContentType type;
    private ContentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Content() {
    }

    private Content(CourseSection section, String title, ContentType type, ContentStatus status) {
        validateSection(section);
        validateTitle(title);
        validateType(type);
        validateStatus(status);

        this.section = section;
        this.title = title;
        this.type = type;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    public void validateSection(CourseSection section) {
        if (section == null) {
            throw new IllegalArgumentException("강의 섹션 값은 필수입니다.");
        }
    }

    public void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("섹션 제목은 필수입니다.");
        }
        if (title.length() < 2 || title.length() > 50) {
            throw new IllegalArgumentException("섹션 제목은 2자 이상 50자 이하여야 합니다.");
        }
    }

    public void validateType(ContentType type) {
        if (type == null) {
            throw new IllegalArgumentException("컨텐츠 타입은 필수 값입니다.");
        }
    }

    public void validateStatus(ContentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("컨텐츠 상태는 필수값입니다.");
        }
        if (status != ContentStatus.NORMAL && status != ContentStatus.HIDDEN) {
            throw new IllegalArgumentException("콘텐츠의 초기 상태는 NORMAL 또는 HIDDEN만 가능합니다.");
        }
    }

    /*
     * 컨텐츠 생성
     * - 생성자에서 유효값 검증 후 Content 객체를 생성한다
     * */
    public static Content create(CourseSection section, String title, ContentType type, ContentStatus status) {
        return new Content(section, title, type, status);
    }

    /*
     * 컨텐츠 조회
     * - DB에 저장된 값을 Content 객체로 복원한다.
     * - 유효값 검증된 객체를 DB에 저장하였고, 그걸 복원하는 과정이므로 빈 생성자를 통해 Content 객체를 생성한다
     * */
    public static Content reconstruct(Long id, CourseSection section, String title, ContentType type, ContentStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Content content = new Content();

        content.id = id;
        content.section = section;
        content.title = title;
        content.type = type;
        content.status = status;
        content.createdAt = createdAt;
        content.updatedAt = updatedAt;

        return content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ContentType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public CourseSection getSection() {
        return section;
    }


}
