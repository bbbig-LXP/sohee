package com.lxp.sohee.user.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String name;
    private UserType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name, UserType type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수값입니다.");
        }
        if (type == null) {
            throw new IllegalArgumentException("사용자 타입은 필수값입니다.");
        }

        this.name = name;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }

    // 강좌 생성 권한 확인
    public void validateInstructorRole() {
        if (this.type != UserType.INSTRUCTOR) {
            throw new RuntimeException("강사 권한이 없습니다. (현재 타입: " + this.type + ")");
        }
    }

    // 수강 등록 권한 확인
    public void validateStudentRole() {
        if (this.type != UserType.STUDENT) {
            throw new RuntimeException("학생만 수강 신청을 할 수 있습니다. (현재 타입: " + this.type + ")");
        }
    }
}
