package com.lxp.sohee.user.model;

public enum UserType {
    STUDENT,
    INSTRUCTOR,
    ADMIN;

    // 사용자 타입 확인 메서드
    public boolean isStudent() {
        return this == STUDENT;
    }

    public boolean isInstructor() {
        return this == INSTRUCTOR;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    // DB에 입력된 값은 VARCHAR(String) 이기 때문에 enum 타입으로 변경
    public static UserType from(String value) {
        if (value == null || value.isBlank()) {
            return STUDENT; // 기본값 설정 or 예외 발생
        }
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("잘못된 사용자 타입입니다: " + value);
        }
    }
}